/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import io.pivotal.arca.threading.Identifier;
import io.pivotal.arca.utils.Logger;

public abstract class Task<T> implements NetworkingTask<T>, NetworkingPrioritizableObserver<T>, ProcessingTask<T>, ProcessingPrioritizableObserver<T> {

    protected static interface Messages {
        public static final String NO_EXECUTOR = "Cannot execute request. No request executor found.";
    }

    private enum State {
        PENDING, STARTING, NETWORKING, PROCESSING, COMPLETE, CANCELLED, FAILED
    }

    private final Object mTaskLock = new Object();
    private final Object mStateLock = new Object();

    private final Set<Task<?>> mPrerequisites = new HashSet<Task<?>>();
    private final Set<Task<?>> mDependencies = new HashSet<Task<?>>();

    private State mState = State.PENDING;
    private Priority mPriority = Priority.MEDIUM;
    private Identifier<?> mIdentifier;

    private TaskObserver mObserver;
    private RequestExecutor mExecutor;
    private Context mContext;

    private ServiceError mError;
    private T mData;

    @Override
    public final Identifier<?> getIdentifier() {
        if (mIdentifier == null) {
            mIdentifier = onCreateIdentifier();
        }
        return mIdentifier;
    }

    public T getData() {
        return mData;
    }

    public ServiceError getError() {
        return mError;
    }

    public Set<Task<?>> getPrerequisites() {
        return mPrerequisites;
    }

    public Set<Task<?>> getDependencies() {
        return mDependencies;
    }

    public void setContext(final Context context) {
        mContext = context;
    }

    public void setPriority(final Priority priority) {
        mPriority = priority;
    }

    public void setTaskObserver(final TaskObserver observer) {
        mObserver = observer;
    }

    public void setRequestExecutor(final RequestExecutor executor) {
        mExecutor = executor;
    }

    public void execute() {
        Logger.v("Task[%s] execute", this);

        changeState(State.STARTING);
    }

    public void cancel() {
        Logger.v("Task[%s] cancel", this);

        changeState(State.CANCELLED);
    }

    private void changeState(final State state) {

        if (isFinished()) return;

        Logger.v("Task[%s] change state : [%s] -> [%s]", this, mState, state);

        synchronized (mStateLock) {
            mState = state;
        }

        switch (state) {
            case STARTING:
                startIfPrerequisitesComplete();
                break;

            case NETWORKING:
                notifyStarted();
                startNetworkingRequest();
                break;

            case PROCESSING:
                startProcessingRequest(mData);
                break;

            case COMPLETE:
                notifyComplete();
                break;

            case FAILED:
                notifyFailure();
                break;

            case CANCELLED:
                notifyCancelled();
                break;
        }
    }

    private void startIfPrerequisitesComplete() {
        if (allPrerequisitesComplete()) {
            changeState(State.NETWORKING);
        }
    }

    private boolean isFinished() {
        synchronized (mStateLock) {
            return mState.compareTo(State.COMPLETE) >= 0;
        }
    }

    private boolean allPrerequisitesComplete() {
        synchronized (mTaskLock) {
            return mPrerequisites.isEmpty();
        }
    }

    // ======================================================


    public abstract Identifier<?> onCreateIdentifier();

    public abstract T onExecuteNetworking(Context context) throws Exception;

    public abstract void onExecuteProcessing(Context context, T data) throws Exception;


    // ======================================================


    public final void addPrerequisite(final Task<?> task) {
        Logger.v("Task[%s] add prerequisite: Task[%s]", this, task);

        synchronized (mTaskLock) {
            if (!mPrerequisites.contains(task)) {
                mPrerequisites.add(task);
                task.addDependency(this);
            }
        }
    }

    public final void addDependency(final Task<?> task) {
        Logger.v("Task[%s] add dependency: Task[%s]", this, task);

        synchronized (mTaskLock) {
            if (!mDependencies.contains(task)) {
                mDependencies.add(task);
                task.addPrerequisite(this);
            }
        }
    }


    // ======================================================


    protected void onPrerequisiteStarted(final Task<?> task) {
        Logger.v("Task[%s] prerequisite started: Task[%s]", this, task);

    }

    protected void onPrerequisiteComplete(final Task<?> task) {
        Logger.v("Task[%s] prerequisite complete: Task[%s]", this, task);

        synchronized (mTaskLock) {
            mPrerequisites.remove(task);
        }

        changeState(State.STARTING);
    }

    protected void onPrerequisiteFailure(final Task<?> task, final ServiceError error) {
        Logger.v("Task[%s] prerequisite failure: Task[%s]", this, task);

        synchronized (mTaskLock) {
            mPrerequisites.remove(task);
        }

        mError = error;

        changeState(State.FAILED);
    }

    protected void onPrerequisiteCancelled(final Task<?> task) {
        Logger.v("Task[%s] prerequisite cancelled: Task[%s]", this, task);

        synchronized (mTaskLock) {
            mPrerequisites.remove(task);
        }

        changeState(State.CANCELLED);
    }


    // ======================================================


    private void startNetworkingRequest() {
        Logger.v("Task[%s] start networking request", this);

        if (mExecutor != null) {
            final NetworkingPrioritizable<T> prioritizable = new NetworkingPrioritizable<T>(this);
            final NetworkingRequest<T> request = new NetworkingRequest<T>(prioritizable, mPriority.ordinal(), this);
            mExecutor.executeNetworkingRequest(request);
        } else {
            throw new IllegalStateException(Messages.NO_EXECUTOR);
        }
    }

    @Override
    public final T executeNetworking() throws Exception {
        return onExecuteNetworking(mContext);
    }

    @Override
    public final void onNetworkingComplete(final T data) {
        Logger.v("Task[%s] networking complete", this);

        mData = data;

        changeState(State.PROCESSING);
    }

    @Override
    public final void onNetworkingFailure(final ServiceError error) {
        Logger.v("Task[%s] networking failure : %s", this, error);

        mError = error;

        changeState(State.FAILED);
    }


    // ======================================================


    private void startProcessingRequest(final T data) {
        Logger.v("Task[%s] start processing request", this);

        if (mExecutor != null) {
            final ProcessingPrioritizable<T> prioritizable = new ProcessingPrioritizable<T>(this, data);
            final ProcessingRequest<T> request = new ProcessingRequest<T>(prioritizable, mPriority.ordinal(), this);
            mExecutor.executeProcessingRequest(request);
        } else {
            throw new IllegalStateException(Messages.NO_EXECUTOR);
        }
    }

    @Override
    public final void executeProcessing(final T data) throws Exception {
        onExecuteProcessing(mContext, data);
    }

    @Override
    public final void onProcessingComplete() {
        Logger.v("Task[%s] processing complete", this);

        changeState(State.COMPLETE);
    }

    @Override
    public final void onProcessingFailure(final ServiceError error) {
        Logger.v("Task[%s] processing failure : %s", this, error);

        mError = error;

        changeState(State.FAILED);
    }


    // ======================================================


    private void notifyStarted() {
        Logger.v("Task[%s] started", this);

        if (mObserver != null) {
            mObserver.onTaskStarted(this);
        }
        notifyDependentsOfStart();
    }

    private void notifyDependentsOfStart() {
        synchronized (mTaskLock) {
            for (final Task<?> dependant : mDependencies) {
                dependant.onPrerequisiteStarted(this);
            }
        }
    }

    private void notifyComplete() {
        Logger.v("Task[%s] complete", this);

        if (mObserver != null) {
            mObserver.onTaskComplete(this);
        }
        notifyDependentsOfCompletion();
    }

    private void notifyDependentsOfCompletion() {
        synchronized (mTaskLock) {
            for (final Task<?> dependant : mDependencies) {
                dependant.onPrerequisiteComplete(this);
            }
        }
    }

    private void notifyCancelled() {
        Logger.v("Task[%s] cancelled", this);

        if (mObserver != null) {
            mObserver.onTaskCancelled(this);
        }
        notifyDependentsOfCancellation();
    }

    private void notifyDependentsOfCancellation() {
        synchronized (mTaskLock) {
            for (final Task<?> dependant : mDependencies) {
                dependant.onPrerequisiteCancelled(this);
            }
        }
    }

    private void notifyFailure() {
        Logger.v("Task[%s] failed", this);

        if (mObserver != null) {
            mObserver.onTaskFailure(this, mError);
        }
        notifyDependentsOfFailure(mError);
    }

    private void notifyDependentsOfFailure(final ServiceError error) {
        synchronized (mTaskLock) {
            for (final Task<?> dependant : mDependencies) {
                dependant.onPrerequisiteFailure(this, error);
            }
        }
    }
}
