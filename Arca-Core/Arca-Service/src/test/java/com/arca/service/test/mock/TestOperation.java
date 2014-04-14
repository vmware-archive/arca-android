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
package com.arca.service.test.mock;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import com.arca.service.Operation;
import com.arca.service.ServiceError;
import com.arca.service.Task;

import java.util.List;
import java.util.Set;

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
