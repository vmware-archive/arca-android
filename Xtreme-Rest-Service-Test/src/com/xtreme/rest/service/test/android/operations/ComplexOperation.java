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

public class ComplexOperation extends Operation {

	public ComplexOperation(int i) {
		super(Uri.parse(String.format("O%d", i)));
	}

	public ComplexOperation(Parcel source) {
		super(source);
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		Log.d(TestActivity.TAG, "onCreateTasks for Operation: " + getUri());

		OneSecondTask[] tasks = new OneSecondTask[15];
		for (int i = 0; i < tasks.length; i++)
			tasks[i] = new OneSecondTask(i);

		// layer 1
		// tasks 0, 1, 2 have no prerequisites

		// layer 2
		tasks[3].addPrerequisite(tasks[0]);
		tasks[3].addPrerequisite(tasks[1]);
		tasks[4].addPrerequisite(tasks[0]);
		tasks[4].addPrerequisite(tasks[1]);
		
		// layer 3
		tasks[5].addPrerequisite(tasks[3]);
		
		// layer 4
		tasks[6].addPrerequisite(tasks[5]);
		tasks[6].addPrerequisite(tasks[4]);
		tasks[6].addPrerequisite(tasks[2]);
		
		// layer 5
		tasks[7].addPrerequisite(tasks[6]);
		tasks[8].addPrerequisite(tasks[6]);
		tasks[9].addPrerequisite(tasks[6]);
		
		// layer 6
		tasks[10].addPrerequisite(tasks[8]);
		
		// layer 7
		tasks[11].addPrerequisite(tasks[10]);
		tasks[12].addPrerequisite(tasks[10]);
		tasks[13].addPrerequisite(tasks[10]);
		
		// layer 8
		tasks[14].addPrerequisite(tasks[7]);
		tasks[14].addPrerequisite(tasks[11]);
		tasks[14].addPrerequisite(tasks[12]);
		tasks[14].addPrerequisite(tasks[13]);
		
		// execute tasks
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
		OneSecondTask.ALWAYS_FAIL = false;
		executeTask(new OneSecondTask(15));
	}

	public static final Creator<ComplexOperation> CREATOR = new Creator<ComplexOperation>() {
		@Override
		public ComplexOperation[] newArray(int size) {
			return new ComplexOperation[size];
		}

		@Override
		public ComplexOperation createFromParcel(Parcel source) {
			return new ComplexOperation(source);
		}
	};

}
