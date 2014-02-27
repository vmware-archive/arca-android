package com.arca.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;

import com.xtreme.threading.RequestIdentifier;

public abstract class Task<T> implements NetworkingTask<T>, NetworkingPrioritizableObserver<T>, ProcessingTask<T>, ProcessingPrioritizableObserver<T> {

	protected static interface Messages {
		public static final String NO_EXECUTOR = "Cannot execute request. No request executor found.";
	}
	
	private final Set<Task<?>> mPrerequisites = new HashSet<Task<?>>();
	private final Set<Task<?>> mDependants = new HashSet<Task<?>>();
	private final List<ServiceError> mErrors = new ArrayList<ServiceError>();
	private final Object mTaskLock = new Object();
			
	private Priority mPriority = Priority.MEDIUM;
	private TaskObserver mObserver;
	private RequestExecutor mExecutor;
	private RequestIdentifier<?> mIdentifier;
	private Context mContext;
	
	@Override
	public final RequestIdentifier<?> getIdentifier() {
		return mIdentifier;
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
		mIdentifier = onCreateIdentifier();
		checkPrerequisites();
	}

	private void checkPrerequisites() {
		if (allPrerequisitesComplete()) {
			checkExecution();
		}	
	}

	private boolean allPrerequisitesComplete() {
		synchronized (mTaskLock) {
			return mPrerequisites.isEmpty();
		}
	}

	private void checkExecution() {
		if (!mErrors.isEmpty()) {
			final ServiceError error = mErrors.get(0); 
			notifyFailure(error);
		} else {
			notifyStarted();
			startNetworkingRequest();
		}
	}

	// ======================================================
	
	public abstract RequestIdentifier<?> onCreateIdentifier();

	public abstract T onExecuteNetworking(Context context) throws Exception;

	public abstract void onExecuteProcessing(Context context, T data) throws Exception;

	// ======================================================

	public final void addPrerequisite(final Task<?> task) {
		synchronized (mTaskLock) {
			if (!mPrerequisites.contains(task)) {
				mPrerequisites.add(task);
				task.addDependant(this);
			}
		}
	}

	public final void addDependant(final Task<?> task) {
		synchronized (mTaskLock) {
			if (!mDependants.contains(task)) {
				mDependants.add(task);
				task.addPrerequisite(this);
			}
		}
	}

	// ======================================================

	protected void onPrerequisiteComplete(final Task<?> task) {
		synchronized (mTaskLock) {
			mPrerequisites.remove(task);
			checkPrerequisites();
		}
	}

	protected void onPrerequisiteFailure(final Task<?> task, final ServiceError error) {
		synchronized (mTaskLock) {
			mPrerequisites.remove(task);
			mErrors.add(error);
			checkPrerequisites();
		}
	}
	
	// ======================================================

	private void startNetworkingRequest() {
		if (mExecutor != null) {
			final NetworkingPrioritizable<T> prioritzable = new NetworkingPrioritizable<T>(this);
			final NetworkingRequest<T> request = new NetworkingRequest<T>(prioritzable, mPriority.ordinal(), this);
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
		if (mObserver != null) {
			mObserver.onTaskComplete(this);
		}
		notifyDependentsOfCompletion();
	}
	
	private void notifyFailure(final ServiceError error) {
		if (mObserver != null) {
			mObserver.onTaskFailure(this, error);
		}
		notifyDependentsOfFailure(error);
	}

	private void notifyDependentsOfCompletion() {
		synchronized (mTaskLock) {
			for (final Task<?> dependant : mDependants) {
				dependant.onPrerequisiteComplete(this);
			}
		}
	}

	private void notifyDependentsOfFailure(final ServiceError error) {
		synchronized (mTaskLock) {
			for (final Task<?> dependant : mDependants) {
				dependant.onPrerequisiteFailure(this, error);
			}
		}
	}
}
