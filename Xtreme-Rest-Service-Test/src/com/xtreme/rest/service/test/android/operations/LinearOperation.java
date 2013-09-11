package com.xtreme.rest.service.test.android.operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;
import com.xtreme.rest.service.test.android.activities.TestActivity;
import com.xtreme.rest.service.test.android.tasks.OneSecondTask;

public class LinearOperation extends Operation {

	private final int[] mIndices;

	public LinearOperation(int id, int[] indices) {
		super(Uri.parse(String.format("O%d", id)));
		this.mIndices = indices;
	}

	public LinearOperation(Parcel source) {
		super(source);
		mIndices = source.createIntArray();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeIntArray(mIndices);
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		Log.d(TestActivity.TAG, "onCreateTasks for Operation: " + getUri());

		OneSecondTask[] tasks = new OneSecondTask[mIndices.length];
		for (int i = 0; i < tasks.length; i++)
			tasks[i] = new OneSecondTask(mIndices[i]);


		for (int i = tasks.length - 1; i > 0; i--)
			tasks[i].addPrerequisite(tasks[i - 1]);


		final Set<Task<?>> set = new HashSet<Task<?>>();
		for (OneSecondTask task : tasks)
			set.add(task);
		
		return set;
	}

	@Override
	public synchronized void onTaskComplete(Task<?> task) {
		Log.d(TestActivity.TAG, "onTaskComplete for operation: " + getUri() + ", " + task);
		super.onTaskComplete(task);
	}

	@Override
	public synchronized void onTaskFailure(Task<?> task, ServiceError error) {
		Log.d(TestActivity.TAG, "onTaskFailure for operation: " + getUri() + ", " + task);
		super.onTaskFailure(task, error);
	}

	@Override
	public synchronized void onTaskStarted(Task<?> task) {
		Log.d(TestActivity.TAG, "onTaskStarted for operation: " + getUri() + ", " + task);
		super.onTaskStarted(task);
	}

	@Override
	public void onSuccess(Context context, List<Task<?>> completed) {
		Log.d(TestActivity.TAG, "Success for operation: " + getUri());
	}

	@Override
	public void onFailure(Context context, ServiceError error) {
		Log.d(TestActivity.TAG, "Failure for operation: " + getUri());
	}

	public static final Creator<LinearOperation> CREATOR = new Creator<LinearOperation>() {
		@Override
		public LinearOperation[] newArray(int size) {
			return new LinearOperation[size];
		}

		@Override
		public LinearOperation createFromParcel(Parcel source) {
			return new LinearOperation(source);
		}
	};

}
