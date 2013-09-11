package com.xtreme.rest.service.test.android.operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.util.Log;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;
import com.xtreme.rest.service.test.android.activities.TestActivity;
import com.xtreme.rest.service.test.android.tasks.OneSecondTask;

public class SimpleOperation extends Operation {

	private final int[] mIndices;

	public SimpleOperation(int i, int[] indices) {
		super(Uri.parse(String.format("O%d", i)));
		this.mIndices = indices;
	}

	public SimpleOperation(Parcel source) {
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

		final Set<Task<?>> set = new HashSet<Task<?>>();
		for (int index : mIndices)
			set.add(new OneSecondTask(index));
		
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

	public static final Creator<SimpleOperation> CREATOR = new Creator<SimpleOperation>() {
		@Override
		public SimpleOperation[] newArray(int size) {
			return new SimpleOperation[size];
		}

		@Override
		public SimpleOperation createFromParcel(Parcel source) {
			return new SimpleOperation(source);
		}
	};

}
