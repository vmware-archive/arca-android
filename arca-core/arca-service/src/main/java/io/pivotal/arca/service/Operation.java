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

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            task.execute();
		}
	}

	// ======================================================

	public abstract Set<Task<?>> onCreateTasks();

	public abstract void onSuccess(Context context, List<Task<?>> completed);

	public abstract void onFailure(Context context, ServiceError error);

	// ======================================================

	@Override
	public void onTaskStarted(final Task<?> task) {
        moveTaskFromPendingToExecuting(task);
	}

	@Override
	public void onTaskComplete(final Task<?> task) {
        moveTaskFromExecutingToCompleted(task);
        handleTaskDependencies(task);
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

	private void handleTaskDependencies(final Task<?> task) {
		final Set<Task<?>> tasks = task.getDependencies();
		addTasksToPending(tasks);
	}

	private void handleTaskFailure(final ServiceError error) {
		// TODO: handle task failures better
		if (error != null) {
			mError = error;
		}
	}

	private void addTaskToPending(final Task<?> task) {
		synchronized (mTaskLock) {
            task.setTaskObserver(this);
            task.setRequestExecutor(mExecutor);
            task.setPriority(mPriority);
            task.setContext(mContext);
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
