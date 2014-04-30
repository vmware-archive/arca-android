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

public class PrioritizableRequest implements Runnable {

	private final int mAccessorIndex;
	private final Prioritizable mPrioritizable;

	public PrioritizableRequest(final Prioritizable prioritizable, final int accessorIndex) {
		if (prioritizable == null || accessorIndex < 0)
			throw new IllegalArgumentException("Cannot pass null prioritizable or negative accessorIndex to constructor.");

		mPrioritizable = prioritizable;
		mAccessorIndex = accessorIndex;
	}

	public int getAccessorIndex() {
		return mAccessorIndex;
	}

	public Prioritizable getPrioritizable() {
		return mPrioritizable;
	}

	@Override
	public void run() {
		mPrioritizable.disableCancel();
		if (!mPrioritizable.isCancelled()) {
			mPrioritizable.execute();
		}
	}

	public boolean isCancelled() {
		return mPrioritizable.isCancelled();
	}

	public Identifier<?> getIdentifier() {
		return mPrioritizable.getIdentifier();
	}

	public boolean cancel() {
		return mPrioritizable.cancel();
	}
}
