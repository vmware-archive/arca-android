package com.arca.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public abstract class Operation implements Parcelable, TaskObserver {
	
	private final Set<Task<?>> mPendingTasks = new HashSet<Task<?>>();
	private final Set<Task<?>> mExecutingTasks = new HashSet<Task<?>>();
	private final List<Task<?>> mCompletedTasks = new ArrayList<Task<?>>();
	private final Object mTaskLock = new Object();

	private final Priority mPriority;
	private final Uri mUri;

	private OperationObserver mObserver;
	private RequestExecutor mExecutor;
	
	private Context mContext;
	private ServiceError mError;

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
	
	public void setRequestExecutor(final RequestExecutor executor) {
		mExecutor = executor;
	}

	public void execute() {
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

	private void addTasksToPending(final Set<Task<?>> tasks) {
		for (final Task<?> task : tasks) {
			addTaskToPending(task);
		}
	}

	private void executeTasks(final Set<Task<?>> tasks) {
		for (final Task<?> task : tasks) {
			executeTaskNow(task);
		}
	}
	
	private void executeTaskNow(final Task<?> task) {
		task.setContext(mContext);
		task.setPriority(mPriority);
		task.setTaskObserver(this);
		task.setRequestExecutor(mExecutor);
		task.execute();
	}

	// ======================================================

	public abstract Set<Task<?>> onCreateTasks();

	public abstract void onSuccess(Context context, List<Task<?>> completed);

	public abstract void onFailure(Context context, ServiceError error);
	
	// ======================================================

	protected void executeTask(final Task<?> task) {
		if (task != null) {
			addTaskToPending(task);
			executeTaskNow(task);
		} else {
			notifyComplete();
		}
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
		if (mObserver != null) {
			mObserver.onOperationComplete(this);
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
	
}
