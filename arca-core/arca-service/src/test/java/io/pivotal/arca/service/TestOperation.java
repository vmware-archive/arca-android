/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.service;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
