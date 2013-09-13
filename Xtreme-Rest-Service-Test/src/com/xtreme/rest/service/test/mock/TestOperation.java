package com.xtreme.rest.service.test.mock;

import java.util.List;
import java.util.Set;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;

public class TestOperation extends Operation {

	private final Set<Task<?>> mTasks;
	
	public TestOperation(final Set<Task<?>> tasks) {
		this(null, tasks);
	}
	
	public TestOperation(final Uri uri, final Set<Task<?>> tasks) {
		super(uri);
		mTasks = tasks;
	}
	
	public TestOperation(final Parcel in) {
		super(in);
		mTasks = null;
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		return mTasks;
	}
	
	@Override
	public void executeTask(final Task<?> task) {
		super.executeTask(task);
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
