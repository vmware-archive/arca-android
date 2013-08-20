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

public class DiamondOperation extends Operation {

	private final int mBegin;
	private final int[] mMiddle;
	private final int mEnd;

	public DiamondOperation(int operationId, int begin, int[] middle, int end) {
		super(Uri.parse(String.format("O%d", operationId)));
		mBegin = begin;
		mMiddle = middle;
		mEnd = end;
	}

	public DiamondOperation(Parcel source) {
		super(source);
		mBegin = source.readInt();
		mMiddle = source.createIntArray();
		mEnd = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(mBegin);
		dest.writeIntArray(mMiddle);
		dest.writeInt(mEnd);
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		Log.d(TestActivity.TAG, "onCreateTasks for Operation: " + getUri());

		OneSecondTask beginning = new OneSecondTask(mBegin);

		OneSecondTask[] middle = new OneSecondTask[mMiddle.length];
		for (int i = 0; i < middle.length; i++)
			middle[i] = new OneSecondTask(mMiddle[i]);

		OneSecondTask end = new OneSecondTask(mEnd);


		for (OneSecondTask task : middle) {
			task.addPrerequisite(beginning);
			end.addPrerequisite(task);
		}

		
		final Set<Task<?>> set = new HashSet<Task<?>>();
		set.add(beginning);
		
		for (OneSecondTask task : middle)
			set.add(task);
		
		set.add(end);
		
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
	
	public static final Creator<DiamondOperation> CREATOR = new Creator<DiamondOperation>() {
		@Override
		public DiamondOperation[] newArray(int size) {
			return new DiamondOperation[size];
		}

		@Override
		public DiamondOperation createFromParcel(Parcel source) {
			return new DiamondOperation(source);
		}
	};

}
