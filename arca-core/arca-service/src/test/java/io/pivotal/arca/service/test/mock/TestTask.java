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
package io.pivotal.arca.service.test.mock;

import android.content.Context;

import io.pivotal.arca.service.Task;
import io.pivotal.arca.threading.Identifier;

public class TestTask extends Task<String> {

	public static interface Messages extends Task.Messages {
	}

	private final Identifier<?> mIdentifier;
	private final Exception mNetworkingException;
	private final Exception mProcessingException;
	private final String mNetworkResult;
	private Task<?> mDependency;

	public TestTask(final Identifier<?> identifier) {
		this(identifier, null, null, null);
	}

	public TestTask(final Identifier<?> identifier, final Task<?> task) {
		this(identifier, null, null, null);
		mDependency = task;
	}

	public TestTask(final Identifier<?> identifier, final String networkResult) {
		this(identifier, networkResult, null, null);
	}

	public TestTask(final Identifier<?> identifier, final String networkResult, final Exception networkingException, final Exception processingException) {
		mIdentifier = identifier;
		mNetworkResult = networkResult;
		mNetworkingException = networkingException;
		mProcessingException = processingException;
	}

	@Override
	public Identifier<?> onCreateIdentifier() {
		return mIdentifier;
	}

	@Override
	public String onExecuteNetworking(final Context context) throws Exception {

		if (mNetworkingException != null) {
			throw mNetworkingException;
		}

		if (mDependency != null) {
			addDependency(mDependency);
		}

		return mNetworkResult;
	}

	@Override
	public void onExecuteProcessing(final Context context, final String data) throws Exception {
		if (mProcessingException != null) {
			throw mProcessingException;
		}
	}
}
