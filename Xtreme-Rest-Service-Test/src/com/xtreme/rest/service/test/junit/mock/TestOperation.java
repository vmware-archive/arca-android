package com.xtreme.rest.service.test.junit.mock;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;

public class TestOperation extends Operation {

	public TestOperation(final Uri uri) {
		super(uri);
	}
	
	public TestOperation(final Parcel in) {
		super(in);
	}

	@Override
	public void onCreateTasks() {
		// do nothing for now
	}
	
	@Override
	public void onTaskStarted(final Task<?> task) {
		super.onTaskStarted(task);
	}
	
	@Override
	public void onTaskComplete(final Task<?> task) {
		super.onTaskComplete(task);
	}
	
	@Override
	public void onTaskFailure(final Task<?> task, final ServiceError error) {
		super.onTaskFailure(task, error);
	}
	
	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		
	}
	
	public static final Parcelable.Creator<TestOperation> CREATOR = new Parcelable.Creator<TestOperation>() {
		@Override
		public TestOperation createFromParcel(final Parcel in) {
			return new TestOperation(in);
		}

		@Override
		public TestOperation[] newArray(final int size) {
			return new TestOperation[size];
		}
	};

}
