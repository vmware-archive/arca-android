/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.pivotal.arca.utils.Logger;

@SuppressLint("ParcelCreator")
public abstract class Operation implements Parcelable, TaskObserver {

    private final Object mTaskLock = new Object();

    private final Set<Task<?>> mPendingTasks = new HashSet<Task<?>>();
    private final Set<Task<?>> mExecutingTasks = new HashSet<Task<?>>();

    private final List<Task<?>> mCompletedTasks = new ArrayList<Task<?>>();
    private final List<Task<?>> mCancelledTasks = new ArrayList<Task<?>>();
    private final List<Task<?>> mFailedTasks = new ArrayList<Task<?>>();

    private final Priority mPriority;
    private final Uri mUri;

    private boolean mIsComplete;
    private OperationObserver mObserver;
    private RequestExecutor mExecutor;
    private Context mContext;


    public Operation(final Uri uri, final Priority priority) {
        mUri = uri;
        mPriority = priority;
    }

    public Operation(final Uri uri) {
        this(uri, Priority.LIVE);
    }

    public Operation(final Parcel in) {
        mUri = in.readParcelable(Uri.class.getClassLoader());
        mPriority = (Priority) in.readSerializable();
    }

    public ServiceError getError() {
        return !mFailedTasks.isEmpty() ? mFailedTasks.get(0).getError() : null;
    }

    public final Uri getUri() {
        return mUri;
    }

    public void setContext(final Context context) {
        mContext = context;
    }

    public void setOperationObserver(final OperationObserver observer) {
        mObserver = observer;
    }

    public void setRequestExecutor(final RequestExecutor executor) {
        mExecutor = executor;
    }

    public void execute() {
        Logger.v("Operation[%s] execute", this);
        mIsComplete = false;
        final Set<Task<?>> tasks = onCreateTasks();
        checkTasks(tasks);
    }

    private void checkTasks(final Set<Task<?>> tasks) {
        if (tasks != null && !tasks.isEmpty()) {
            addTasksToPending(tasks);
            executeTasks(tasks);
        } else {
            notifyComplete();
        }
    }

    private void executeTasks(final Set<Task<?>> tasks) {
        for (final Task<?> task : tasks) {
            task.execute();
        }
    }

    public void cancel() {
        Logger.v("Operation[%s] cancel", this);

        final Set<Task<?>> pendingTasks = new HashSet<Task<?>>();
        final Set<Task<?>> executingTasks = new HashSet<Task<?>>();

        synchronized (mTaskLock) {
            pendingTasks.addAll(mPendingTasks);
            executingTasks.addAll(mExecutingTasks);
        }

        for (final Task pendingTask : pendingTasks) {
            pendingTask.cancel();
        }

        for (final Task executingTask : executingTasks) {
            executingTask.cancel();
        }
    }


    // ======================================================


    @Override
    public void onTaskStarted(final Task<?> task) {
        Logger.v("Operation[%s] Task[%s] started", this, task);

        moveTaskToExecuting(task);
    }

    @Override
    public void onTaskComplete(final Task<?> task) {
        Logger.v("Operation[%s] Task[%s] complete", this, task);

        moveTaskToCompleted(task);
        handleTaskDependencies(task);
        checkTaskCompletion();
    }

    @Override
    public void onTaskFailure(final Task<?> task, final ServiceError error) {
        Logger.v("Operation[%s] Task[%s] failure", this, task);

        moveTaskToFailed(task);
        checkTaskCompletion();
    }

    @Override
    public void onTaskCancelled(final Task<?> task) {
        Logger.v("Operation[%s] Task[%s] cancelled", this, task);

        moveTaskToCancelled(task);
        checkTaskCompletion();
    }


    // ======================================================


    private void checkTaskCompletion() {
        Logger.v("Operation[%s] checking task completion", this);
        if (!mIsComplete && allTasksComplete()) {
            notifyComplete();
        }
    }

    private boolean allTasksComplete() {
        synchronized (mTaskLock) {
            return mExecutingTasks.isEmpty() && mPendingTasks.isEmpty();
        }
    }

    private void handleTaskDependencies(final Task<?> task) {
        Logger.v("Operation[%s] Task[%s] add task dependencies", this, task);
        final Set<Task<?>> tasks = task.getDependencies();
        addTasksToPending(tasks);
    }

    private void addTasksToPending(final Set<Task<?>> tasks) {
        Logger.v("Operation[%s] add tasks to pending [%d]", this, tasks.size());

        for (final Task<?> task : tasks) {
            addTaskToPending(task);
        }
    }

    private void addTaskToPending(final Task<?> task) {
        Logger.v("Operation[%s] Task[%s] add to pending", this, task);

        synchronized (mTaskLock) {
            if (!mCancelledTasks.contains(task)) {
                task.setTaskObserver(this);
                task.setRequestExecutor(mExecutor);
                task.setPriority(mPriority);
                task.setContext(mContext);
                mPendingTasks.add(task);
            }
        }
    }

    private void moveTaskToExecuting(final Task<?> task) {
        Logger.v("Operation[%s] Task[%s] moved to executing", this, task);

        synchronized (mTaskLock) {
            if (mPendingTasks.remove(task)) {
                mExecutingTasks.add(task);
            }
        }
    }

    private void moveTaskToCompleted(final Task<?> task) {
        Logger.v("Operation[%s] Task[%s] moved to completed", this, task);

        synchronized (mTaskLock) {
            if (mPendingTasks.remove(task) || mExecutingTasks.remove(task)) {
                mCompletedTasks.add(task);
            }
        }
    }

    private void moveTaskToFailed(final Task<?> task) {
        Logger.v("Operation[%s] Task[%s] moved to failed", this, task);

        synchronized (mTaskLock) {
            if (mPendingTasks.remove(task) || mExecutingTasks.remove(task)) {
                mFailedTasks.add(task);
            }
        }
    }

    private void moveTaskToCancelled(final Task<?> task) {
        Logger.v("Operation[%s] Task[%s] moved to cancelled", this, task);

        synchronized (mTaskLock) {
            if (mPendingTasks.remove(task) || mExecutingTasks.remove(task)) {
                mCancelledTasks.add(task);
            }
        }
    }

    private void notifyComplete() {
        Logger.v("Operation[%s] notify complete", this);
        mIsComplete = true;

        if (hasCancelledTasks()) {
            Logger.v("Operation[%s] notify cancelled", this);
            onFailure(mContext, new ServiceError("Operation Cancelled"));

        } else if (hasFailedTasks()) {
            Logger.v("Operation[%s] notify failure", this);
            onFailure(mContext, mFailedTasks.get(0).getError());

        } else if (hasCompletedTasks()) {
            Logger.v("Operation[%s] notify success", this);
            onSuccess(mContext, mCompletedTasks);
        }

        onComplete(mContext, new Results(mCompletedTasks, mCancelledTasks, mFailedTasks));

        if (mObserver != null) {
            Logger.v("Operation[%s] notify observer complete", this);
            mObserver.onOperationComplete(this);
        }
    }

    private boolean hasFailedTasks() {
        synchronized (mTaskLock) {
            return !mFailedTasks.isEmpty();
        }
    }

    private boolean hasCancelledTasks() {
        synchronized (mTaskLock) {
            return !mCancelledTasks.isEmpty();
        }
    }

    private boolean hasCompletedTasks() {
        synchronized (mTaskLock) {
            return !mCompletedTasks.isEmpty();
        }
    }


    // ======================================================

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(mUri, flags);
        dest.writeSerializable(mPriority);
    }

    public abstract Set<Task<?>> onCreateTasks();

    public void onComplete(Context context, Results results) {}

    @Deprecated
    public void onSuccess(Context context, List<Task<?>> completed) {}

    @Deprecated
    public void onFailure(Context context, ServiceError error) {}


    // ======================================================


    public static class Results {

        private final List<Task<?>> mCompletedTasks = new ArrayList<Task<?>>();
        private final List<Task<?>> mCancelledTasks = new ArrayList<Task<?>>();
        private final List<Task<?>> mFailedTasks = new ArrayList<Task<?>>();

        public Results(final List<Task<?>> completed, final List<Task<?>> cancelled, final List<Task<?>> failed) {
            mCompletedTasks.addAll(completed);
            mCancelledTasks.addAll(cancelled);
            mFailedTasks.addAll(failed);
        }

        public List<Task<?>> getCompletedTasks() {
            return mCompletedTasks;
        }

        public List<Task<?>> getCancelledTasks() {
            return mCancelledTasks;
        }

        public List<Task<?>> getFailedTasks() {
            return mFailedTasks;
        }

        public boolean hasCompletedTasks() {
            return !mCancelledTasks.isEmpty();
        }

        public boolean hasCancelledTasks() {
            return !mCancelledTasks.isEmpty();
        }

        public boolean hasFailedTasks() {
            return !mFailedTasks.isEmpty();
        }

        public boolean isSuccess() {
            return !hasCancelledTasks() && !hasFailedTasks();
        }
    }
}
