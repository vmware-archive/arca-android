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
package io.pivotal.arca.threading;

public abstract class Prioritizable {
	private volatile boolean mIsCancelled = false;
	private volatile boolean mCancelable = true;

	public abstract Identifier<?> getIdentifier();

	public abstract void execute();

	final synchronized boolean isCancelled() {
		return mIsCancelled;
	}

	final synchronized boolean cancel() {
		if (mCancelable) {
			mIsCancelled = true;
		}
		return mIsCancelled;
	}

	final void disableCancel() {
		mCancelable = false;
	}
}
