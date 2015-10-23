/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.pivotal.arca.threading.Identifier;

public class TestOperation extends Operation {

	private final Set<Task<?>> mTasks;

    private List<Task<?>> mCompletedTasks = new ArrayList<Task<?>>();
    private List<Task<?>> mFailedTasks = new ArrayList<Task<?>>();
    private List<Task<?>> mCancelledTasks = new ArrayList<Task<?>>();

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
	public Identifier<?> onCreateIdentifier() {
		return new Identifier<String>("operation");
	}

	public List<Task<?>> getCompletedTasks() {
        return mCompletedTasks;
    }

    public List<Task<?>> getFailedTasks() {
        return mFailedTasks;
    }

    public List<Task<?>> getCancelledTasks() {
        return mCancelledTasks;
    }

    @Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {

	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {

	}

    @Override
    public void onComplete(final Context context, final Results results) {
        mCompletedTasks = results.getCompletedTasks();
        mCancelledTasks = results.getCancelledTasks();
        mFailedTasks = results.getFailedTasks();
    }

    public static final Creator<TestOperation> CREATOR = new Creator<TestOperation>() {
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
