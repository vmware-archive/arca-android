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
package io.pivotal.arca.dispatcher;

public abstract class Result<T> {

	private final T mData;
	private final Error mError;

	private boolean mIsSyncing = false;
	private boolean mIsValid = true;

	public Result(final T data) {
		mData = data;
		mError = null;
	}

	public Result(final Error error) {
		mData = null;
		mError = error;
	}

	public T getResult() {
		return mData;
	}

	public Error getError() {
		return mError;
	}

	public boolean hasError() {
		return mError != null;
	}

	public void setIsSyncing(final boolean syncing) {
		mIsSyncing = syncing;
	}

	public boolean isSyncing() {
		return mIsSyncing;
	}

	public void setIsValid(final boolean valid) {
		mIsValid = valid;
	}

	public boolean isValid() {
		return mIsValid && !hasError();
	}
}
