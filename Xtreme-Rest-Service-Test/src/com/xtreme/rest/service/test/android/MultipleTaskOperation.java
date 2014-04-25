package com.xtreme.rest.service.test.android;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.util.Log;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;

public class MultipleTaskOperation extends Operation {

	private final int[] mIndices;

	public MultipleTaskOperation(int i, int[] indices) {
		super(Uri.parse(String.format("O%d", i)));
		this.mIndices = indices;
	}

	public MultipleTaskOperation(Parcel source) {
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

		for (int index : mIndices)
			executeTask(new OneSecondTask(index));
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

	public static final Creator<MultipleTaskOperation> CREATOR = new Creator<MultipleTaskOperation>() {
		@Override
		public MultipleTaskOperation[] newArray(int size) {
			return new MultipleTaskOperation[size];
		}

		@Override
		public MultipleTaskOperation createFromParcel(Parcel source) {
			return new MultipleTaskOperation(source);
		}
	};

}
