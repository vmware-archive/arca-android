package com.xtreme.rest.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xtreme.threading.AuxiliaryExecutor;
import com.xtreme.threading.RequestIdentifier;

/**
 * A Task consists of two components: network and processing.</br>
 * </br>
 * The network component is performed in {@link #onExecuteNetworking(Context)} 
 * and is used to download data from the network, then parse the data into the 
 * generic specified. The processing component then stores the data if necessary.</br>
 * </br>
 * Please perform any CPU-intensive processing in {@link #onExecuteProcessing(Context, Object)}
 * instead of {@link #onExecuteNetworking(Context)} as it may cause UI lag. This includes 
 * any calls to the database.
 * 
 * @param <T> The type downloaded, parsed, and returned from {@link #onExecuteNetworking(Context)}.
 */
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
	
	/**
	 * This method must return a globally unique {@link RequestIdentifier}. This is used within
	 * the {@link AuxiliaryExecutor} and ensures that if more than one {@link Task} is processing 
	 * the same data, only one actually executes and the results are then shared among all 
	 * {@link Operation}s "executing" that {@link Task}.
	 * 
	 * @return The unique {@link RequestIdentifier}
	 */
	public abstract RequestIdentifier<?> onCreateIdentifier();
	
	/**
	 * This method is meant to execute network-dependent code (or anything that yields the 
	 * {@link Thread} frequently and is light on the CPU). For example, this is where your 
	 * model would download data from some API.</br>
	 * </br>
	 * Note: All processing should happen synchronously within this method.
	 * 
	 * @param context The context in which this {@link Task} is running
	 * @return The result of the network request
	 * @throws Exception
	 */
	public abstract T onExecuteNetworking(Context context) throws Exception;

	/**
	 * This method is meant to execute CPU-intensive processing requests. For example, this 
	 * is where one would process a {@link String} or insert data into a {@link ContentProvider} 
	 * backed by a {@link SQLiteDatabase} via the {@link ContentResolver}.</br>
	 * </br>
	 * Note: All processing should happen synchronously within this method.
	 * 
	 * @param context The context in which this {@link Task} is running
	 * @param data The result returned from {@link #onExecuteNetworking(Context)}
	 * @throws Exception
	 */
	public abstract void onExecuteProcessing(Context context, T data) throws Exception;

	// ======================================================

	/**
	 * Adds a prerequisite. This {@link Task} will wait until all prerequisites are 
	 * finished before executing.</br>
	 * </br>
	 * Note: By adding a prerequisite, the necessary dependants are also added 
	 * to the task parameter.
	 * 
	 * @param task The prerequisite {@link Task}
	 */
	public final void addPrerequisite(final Task<?> task) {
		synchronized (mTaskLock) {
			if (!mPrerequisites.contains(task)) {
				mPrerequisites.add(task);
				task.addDependant(this);
			}
		}
	}

	/**
	 * Adds a dependant. The {@link Task} will notify all dependants when it has completed 
	 * so that they may execute.</br>
	 * </br>
	 * Note: By adding a dependant, the necessary prerequisites are also added 
	 * to the task parameter.
	 * 
	 * @param task The dependant {@link Task}
	 */
	public final void addDependant(final Task<?> task) {
		synchronized (mTaskLock) {
			if (!mDependants.contains(task)) {
				mDependants.add(task);
				task.addPrerequisite(this);
			}
		}
	}

	// ======================================================

	/**
	 * A callback for when a certain prerequisite has completed successfully.</br>
	 * </br>
	 * Note that this method is called on the {@link Thread} on which it is being executed.
	 * 
	 * @param task The completed prerequisite {@link Task}
	 */
	protected void onPrerequisiteComplete(final Task<?> task) {
		synchronized (mTaskLock) {
			mPrerequisites.remove(task);
			checkPrerequisites();
		}
	}

	/**
	 * A callback for when a certain prerequisite has completed with an error.</br>
	 * </br>
	 * Note that this method is called on the {@link Thread} on which it is being executed.
	 * 
	 * @param task The failed prerequisite {@link Task}
	 * @param error The {@link ServiceError} that has occurred
	 */
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
