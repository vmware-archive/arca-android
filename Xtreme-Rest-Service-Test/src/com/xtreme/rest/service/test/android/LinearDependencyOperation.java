package com.xtreme.rest.service.test.android;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.util.Log;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;

public class LinearDependencyOperation extends Operation {

	private final int[] mIndices;

	public LinearDependencyOperation(int id, int[] indices) {
		super(Uri.parse(String.format("O%d", id)));
		this.mIndices = indices;
	}

	public LinearDependencyOperation(Parcel source) {
		super(source);
		mIndices = source.createIntArray();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeIntArray(mIndices);
	}

	@Override
	public void onCreateTasks() {
		Log.d(Tests.TAG, "onCreateTasks for Operation: " + getUri());

		OneSecondTask[] tasks = new OneSecondTask[mIndices.length];
		for (int i = 0; i < tasks.length; i++)
			tasks[i] = new OneSecondTask(mIndices[i]);


		for (int i = tasks.length - 1; i > 0; i--)
			tasks[i].addPrerequisite(tasks[i - 1]);


		for (OneSecondTask task : tasks)
			executeTask(task);
	}

	@Override
	public synchronized void onTaskComplete(Task<?> task) {
		Log.d(Tests.TAG, "onTaskComplete for operation: " + getUri() + ", " + task);
		super.onTaskComplete(task);
	}

	@Override
	public synchronized void onTaskFailure(Task<?> task, ServiceError error) {
		Log.d(Tests.TAG, "onTaskFailure for operation: " + getUri() + ", " + task);
		super.onTaskFailure(task, error);
	}

	@Override
	public synchronized void onTaskStarted(Task<?> task) {
		Log.d(Tests.TAG, "onTaskStarted for operation: " + getUri() + ", " + task);
		super.onTaskStarted(task);
	}

	@Override
	public void onSuccess(Context context, List<Task<?>> completed) {
		Log.d(Tests.TAG, "Success for operation: " + getUri());
	}

	@Override
	public void onFailure(Context context, ServiceError error) {
		Log.d(Tests.TAG, "Failure for operation: " + getUri());
	}

	public static final Creator<LinearDependencyOperation> CREATOR = new Creator<LinearDependencyOperation>() {
		@Override
		public LinearDependencyOperation[] newArray(int size) {
			return new LinearDependencyOperation[size];
		}

		@Override
		public LinearDependencyOperation createFromParcel(Parcel source) {
			return new LinearDependencyOperation(source);
		}
	};

}
