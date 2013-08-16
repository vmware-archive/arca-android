package com.xtreme.rest.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A collection of {@link Task}s that, when executed, update the data associated with a certain {@link Uri}.
 * When started via the {@link RestService}, an Operation's {@link Task}s begin executing. Dependencies
 * can be set up between tasks in {@link #onCreateTasks()}. When the final {@link Task} finishes executing,
 * the Operation is considered done and then {@link #onSuccess(Context, List)} or
 * {@link #onFailure(Context, ServiceError)} is called.
 */
@SuppressLint("ParcelCreator")
public abstract class Operation implements Parcelable, TaskObserver {
	
	private final Set<Task<?>> mPendingTasks = new HashSet<Task<?>>();
	private final Set<Task<?>> mExecutingTasks = new HashSet<Task<?>>();
	private final List<Task<?>> mCompletedTasks = new ArrayList<Task<?>>();
	private final Object mTaskLock = new Object();

	private final Priority mPriority;
	private final Uri mUri;

	private OperationObserver mObserver;
	private RequestHandler mHandler;
	
	private Context mContext;
	private ServiceError mError;

	/**
	 * Creates a new {@link Operation} with the specified {@link Uri} and {@link Priority}.
	 * 
	 * @param uri The associated {@link Uri}
	 * @param priority The associated {@link Priority}
	 */
	public Operation(final Uri uri, final Priority priority) {
		mUri = uri;
		mPriority = priority;
	}

	/**
	 * Creates a new {@link Operation} with the specified {@link Uri} and the default {@link Priority}.
	 * 
	 * @param uri The associated {@link Uri}
	 */
	public Operation(final Uri uri) {
		mUri = uri;
		mPriority = Priority.LIVE;
	}

	/**
	 * Creates a new {@link Operation} from the provided {@link Parcel}.
	 * 
	 * @param in The {@link Parcel} containing all necessary data.
	 */
	public Operation(final Parcel in) {
		mUri = in.readParcelable(Uri.class.getClassLoader());
		mPriority = (Priority) in.readSerializable();
	}

	public final Uri getUri() {
		return mUri;
	}
	
	public final ServiceError getError() {
		return mError;
	}

	public void setContext(final Context context) {
		mContext = context;
	}

	public void setOperationObserver(final OperationObserver observer) {
		mObserver = observer;
	}
	
	public void setRequestHandler(final RequestHandler handler) {
		mHandler = handler;
	}

	public void execute() {
		try {
			mError = null;
			onCreateTasks();
		} catch (final Exception e) {
			Logger.ex(e);
			mError = new ServiceError(e);
		}
		checkTaskCompletion();
	}

	// ======================================================
	
	/**
	 * This is where all {@link Task}s should be created, their dependencies set up, and then executed. To execute
	 * a {@link Task}, call {@link #executeTask(Task)}.
	 */
	public abstract void onCreateTasks();

	/**
	 * The final callback once all {@link Task}s have been completed successfully.
	 * 
	 * @param context The context in which this {@link Operation} is running
	 * @param completed The completed {@link Task}s
	 */
	public abstract void onSuccess(Context context, List<Task<?>> completed);

	/**
	 * A callback for when all {@link Task}s have finished executing but at least one has failed. You may create recovery
	 * {@link Task}s and call {@link #executeTask(Task)} here.</br>
	 * </br>
	 * IMPORTANT: If the recovery task fails, there will be another callback into this method. Make sure that you don't
	 * attempt to recover again, or this operation will never finish.
	 * 
	 * @param context The context in which this {@link Operation} is running
	 * @param error The error that caused the failure.
	 */
	public abstract void onFailure(Context context, ServiceError error);
	
	// ======================================================

	/**
	 * Adds a {@link Task} to the queues to be executed. This method must be called on all {@link Task}s
	 * that should execute. This method should only be called after dependencies have been set up, usually
	 * in {@link #onCreateTasks()} or {@link #onFailure(Context, ServiceError)}.
	 * 
	 * @param task The {@link Task} to be executed.
	 */
	public void executeTask(final Task<?> task) {
		addTaskToPending(task);
		task.setContext(mContext);
		task.setPriority(mPriority);
		task.setTaskObserver(this);
		task.setRequestHandler(mHandler);
		task.execute();
	}

	@Override
	public void onTaskStarted(final Task<?> task) {
		moveTaskFromPendingToExecuting(task);
	}

	@Override
	public void onTaskComplete(final Task<?> task) {
		moveTaskFromExecutingToCompleted(task);
		checkTaskCompletion();
	}

	@Override
	public void onTaskFailure(final Task<?> task, final ServiceError error) {
		removeTaskFromExecutingAndPending(task);
		handleTaskFailure(error);
		checkTaskCompletion();
	}

	// ======================================================
	
	
	private void checkTaskCompletion() {
		if (allTasksComplete()) {
			notifyComplete();
		}
	}

	private boolean allTasksComplete() {
		synchronized (mTaskLock) {
			return mExecutingTasks.isEmpty() && mPendingTasks.isEmpty();
		}
	}
	
	private void handleTaskFailure(final ServiceError error) {
		// TODO handle task failures better
		if (error != null) { 
			mError = error;
		}
	}
	
	private void addTaskToPending(final Task<?> task) {
		synchronized (mTaskLock) {
			mPendingTasks.add(task);
		}
	}
	
	private void moveTaskFromPendingToExecuting(final Task<?> task) {
		synchronized (mTaskLock) {
			mPendingTasks.remove(task);
			mExecutingTasks.add(task);
		}
	}
	
	private void moveTaskFromExecutingToCompleted(final Task<?> task) {
		synchronized (mTaskLock) {
			mExecutingTasks.remove(task);
			mCompletedTasks.add(task);
		}
	}
	
	private void removeTaskFromExecutingAndPending(final Task<?> task) {
		synchronized (mTaskLock) {
			mExecutingTasks.remove(task);
			mPendingTasks.remove(task);
		}
	}
	
	private void notifyComplete() {
		if (mError == null) {
			onSuccess(mContext, mCompletedTasks);
		} else {
			onFailure(mContext, mError);
		}
		mObserver.onOperationComplete(this);
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

	@Override
	public String toString() {
		return String.format("[%s] %s", getUri(), super.toString());
	}

}
