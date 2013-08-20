package com.xtreme.rest.service;

import java.io.InputStream;
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
 * A Task consists of two components: a network request, and a processing request.</br>
 * The network request occurs in {@link #onExecuteNetworkRequest(Context)} and is used to download data from a network,
 * and is then parsed into the generic specified. The processing request then processes the data that was just downloaded
 * and stores it if necessary.</br>
 * </br>
 * Please keep in mind that no CPU-intensive processing should happen in {@link #onExecuteNetworkRequest(Context)}, as that
 * may cause UI lag. All of that work should be done in {@link #onExecuteProcessingRequest(Context, Object)}.
 * 
 * @param <T> The type downloaded, parsed, and then returned from {@link #onExecuteNetworkRequest(Context)}. In most cases it is a model
 * class, but sometimes it may be a {@link String}.
 */
public abstract class Task<T> implements NetworkHandler<T>, NetworkObserver<T>, ProcessingHandler<T>, ProcessingObserver<T> {

	private static final class Messages {
		private static final String NO_HANDLER = "Cannot execute request. No handler found.";
	}
	
	private final Set<Task<?>> mPrerequisites = new HashSet<Task<?>>();
	private final Set<Task<?>> mDependants = new HashSet<Task<?>>();
	private final List<ServiceError> mErrors = new ArrayList<ServiceError>();
	private final Object mTaskLock = new Object();
			
	private Priority mPriority = Priority.MEDIUM;
	private TaskObserver mObserver;
	private RequestHandler mHandler;
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
	
	public void setRequestHandler(final RequestHandler handler) {
		mHandler = handler;
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
			startNetworkRequest();
		}
	}

	// ======================================================
	
	/**
	 * This method must return a globally unique {@link RequestIdentifier}. This is used within the {@link AuxiliaryExecutor}
	 * and ensures that if more than one {@link Task} is processing the same data, only one actually executes and the results
	 * are then shared among all {@link Operation}s "executing" that {@link Task}.
	 * 
	 * @return The unique {@link RequestIdentifier}
	 */
	public abstract RequestIdentifier<?> onCreateIdentifier();
	
	/**
	 * This method is meant to execute network-dependent code (or anything that yields the {@link Thread} frequently and is
	 * light on the CPU). For example, this is where your model would be downloaded and parsed from the API.</br>
	 * </br>
	 * Note that you should try to parse straight from the {@link InputStream} instead of converting it to a {@link String}
	 * and then parsing that. This is to avoid doing any CPU-intensive processing in this method, such as parsing or string
	 * processing. If it is absolutely necessary, then use this method to only convert the {@link InputStream} to a {@link String},
	 * and then process the {@link String} and parse it in {@link #onExecuteProcessingRequest(Context, Object)}. In this
	 * case, you would want to make the generic for this class a {@link String}.</br>
	 * </br>
	 * All network processing must happen synchronously within this method.
	 * 
	 * @param context The context in which this {@link Task} is running
	 * @return The result of the network request
	 * @throws Exception
	 */
	public abstract T onExecuteNetworkRequest(Context context) throws Exception;

	/**
	 * This method is meant to execute CPU-intensive processing requests. For example, this is where one would process a {@link String} or
	 * insert data into a {@link ContentProvider} backed by a {@link SQLiteDatabase} via the {@link ContentResolver}.</br>
	 * </br>
	 * All processing must happen synchronously within this method.
	 * 
	 * @param context The context in which this {@link Task} is running
	 * @param data The result returned from {@link #onExecuteNetworkRequest(Context)}
	 * @throws Exception
	 */
	public abstract void onExecuteProcessingRequest(Context context, T data) throws Exception;

	// ======================================================

	/**
	 * Adds a prerequisite. This {@link Task} will wait until all prerequisites are finished before being executed.
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
	 * Adds a dependant. The {@link Task} will notify all dependants when it has completed so they may execute.
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

	private void startNetworkRequest() {
		if (mHandler != null) {
			final NetworkPrioritizable<T> prioritzable = new NetworkPrioritizable<T>(this);
			final NetworkRequest<T> request = new NetworkRequest<T>(prioritzable, mPriority.ordinal(), this);
			mHandler.executeNetworkRequest(request);
		} else {
			notifyFailure(new ServiceError(Messages.NO_HANDLER));
		}
	}

	@Override
	public final T executeNetworkRequest() throws Exception {
		return onExecuteNetworkRequest(mContext);
	}

	@Override
	public final void onNetworkRequestComplete(final T data) {
		startProcessingRequest(data);
	}

	@Override
	public final void onNetworkRequestFailure(final ServiceError error) {
		notifyFailure(error);
	}

	// ======================================================

	private void startProcessingRequest(final T data) {
		if (mHandler != null) {
			final ProcessingPrioritizable<T> prioritizable = new ProcessingPrioritizable<T>(this, data);
			final ProcessingRequest<T> request = new ProcessingRequest<T>(prioritizable, mPriority.ordinal(), this);
			mHandler.executeProcessingRequest(request);
		} else {
			notifyFailure(new ServiceError(Messages.NO_HANDLER));
		}
	}

	@Override
	public final void executeProcessingRequest(final T data) throws Exception {
		onExecuteProcessingRequest(mContext, data);
	}

	@Override
	public final void onProcessingRequestComplete() {
		notifyComplete();
	}

	@Override
	public final void onProcessingRequestFailure(final ServiceError error) {
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
	
	@Override
	public String toString() {
		return String.format("[%s] %s", getIdentifier().getData().toString(), super.toString());
	}
}
