/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.service;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.pivotal.arca.threading.Identifier;

public abstract class Task<T> implements NetworkingTask<T>, NetworkingPrioritizableObserver<T>, ProcessingTask<T>, ProcessingPrioritizableObserver<T> {

    protected static interface Messages {
		public static final String NO_EXECUTOR = "Cannot execute request. No request executor found.";
	}

    private final Object mTaskLock = new Object();
    private final Set<Task<?>> mPrerequisites = new HashSet<Task<?>>();
    private final Set<Task<?>> mDependencies = new HashSet<Task<?>>();
    private final List<ServiceError> mErrors = new ArrayList<ServiceError>();

    private Priority mPriority = Priority.MEDIUM;
    private Identifier<?> mIdentifier;
    private boolean mFinished;

    private TaskObserver mObserver;
    private RequestExecutor mExecutor;
	private Context mContext;

	@Override
	public final Identifier<?> getIdentifier() {
        if (mIdentifier == null) {
            mIdentifier = onCreateIdentifier();
        }
        return mIdentifier;
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
        checkExecution();
    }

    private void checkExecution() {
        if (!mFinished && allPrerequisitesComplete()) {
            if (!mErrors.isEmpty()) {
                final ServiceError error = mErrors.get(0);
                notifyFailure(error);
            } else {
                notifyStarted();
                startNetworkingRequest();
            }
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
		synchronized (mTaskLock) {
			if (!mPrerequisites.contains(task)) {
				mPrerequisites.add(task);
				task.addDependency(this);
			}
		}
	}

	public final void addDependency(final Task<?> task) {
		synchronized (mTaskLock) {
			if (!mDependencies.contains(task)) {
				mDependencies.add(task);
				task.addPrerequisite(this);
			}
		}
	}

	// ======================================================

	protected void onPrerequisiteComplete(final Task<?> task) {
		synchronized (mTaskLock) {
			mPrerequisites.remove(task);
            checkExecution();
		}
	}

	protected void onPrerequisiteFailure(final Task<?> task, final ServiceError error) {
		synchronized (mTaskLock) {
			mPrerequisites.remove(task);
			mErrors.add(error);
            checkExecution();
		}
	}

	// ======================================================

	private void startNetworkingRequest() {
		if (mExecutor != null) {
			final NetworkingPrioritizable<T> prioritizable = new NetworkingPrioritizable<T>(this);
			final NetworkingRequest<T> request = new NetworkingRequest<T>(prioritizable, mPriority.ordinal(), this);
			mExecutor.executeNetworkingRequest(request);
		} else {
			notifyFailure(new ServiceError(Messages.NO_EXECUTOR));
		}
	}

	@Override
	public final T executeNetworking() throws Exception {
		return onExecuteNetworking(mContext);
	}

	@Override
	public final void onNetworkingComplete(final T data) {
		startProcessingRequest(data);
	}

	@Override
	public final void onNetworkingFailure(final ServiceError error) {
		notifyFailure(error);
	}

	// ======================================================

	private void startProcessingRequest(final T data) {
		if (mExecutor != null) {
			final ProcessingPrioritizable<T> prioritizable = new ProcessingPrioritizable<T>(this, data);
			final ProcessingRequest<T> request = new ProcessingRequest<T>(prioritizable, mPriority.ordinal(), this);
			mExecutor.executeProcessingRequest(request);
		} else {
			notifyFailure(new ServiceError(Messages.NO_EXECUTOR));
		}
	}

	@Override
	public final void executeProcessing(final T data) throws Exception {
		onExecuteProcessing(mContext, data);
	}

	@Override
	public final void onProcessingComplete() {
		notifyComplete();
	}

	@Override
	public final void onProcessingFailure(final ServiceError error) {
		notifyFailure(error);
	}

	// ======================================================

	private void notifyStarted() {
		if (mObserver != null) {
			mObserver.onTaskStarted(this);
        }
	}

	private void notifyComplete() {
        mFinished = true;

        if (mObserver != null) {
			mObserver.onTaskComplete(this);
        }
		notifyDependentsOfCompletion();
	}

	private void notifyFailure(final ServiceError error) {
        mFinished = true;

        if (mObserver != null) {
			mObserver.onTaskFailure(this, error);
        }
		notifyDependentsOfFailure(error);
	}

	private void notifyDependentsOfCompletion() {
		synchronized (mTaskLock) {
			for (final Task<?> dependant : mDependencies) {
				dependant.onPrerequisiteComplete(this);
			}
		}
	}

	private void notifyDependentsOfFailure(final ServiceError error) {
		synchronized (mTaskLock) {
			for (final Task<?> dependant : mDependencies) {
				dependant.onPrerequisiteFailure(this, error);
			}
		}
	}
}
