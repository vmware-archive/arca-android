package com.xtreme.rest.service.test.android;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.util.Log;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;

public class ReallyComplexOperation extends Operation {

	public ReallyComplexOperation(int i) {
		super(Uri.parse(String.format("O%d", i)));
	}

	public ReallyComplexOperation(Parcel source) {
		super(source);
	}

	@Override
	public void onCreateTasks() {
		Log.d(Tests.TAG, "onCreateTasks for Operation: " + getUri());

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
		OneSecondTask.ALWAYS_FAIL = false;
		executeTask(new OneSecondTask(15));
	}

	public static final Creator<ReallyComplexOperation> CREATOR = new Creator<ReallyComplexOperation>() {
		@Override
		public ReallyComplexOperation[] newArray(int size) {
			return new ReallyComplexOperation[size];
		}

		@Override
		public ReallyComplexOperation createFromParcel(Parcel source) {
			return new ReallyComplexOperation(source);
		}
	};

}
