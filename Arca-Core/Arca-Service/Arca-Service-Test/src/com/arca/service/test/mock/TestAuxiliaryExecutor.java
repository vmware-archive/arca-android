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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.arca.threading.AuxiliaryExecutor;
import com.arca.threading.AuxiliaryExecutorObserver;
import com.arca.threading.PrioritizableRequest;
import com.arca.threading.Identifier;

public class TestAuxiliaryExecutor implements AuxiliaryExecutor {

	private final Set<Identifier<?>> mIdentifiers = new HashSet<Identifier<?>>();
	private final AuxiliaryExecutorObserver mObserver;

	public TestAuxiliaryExecutor(final AuxiliaryExecutorObserver observer) {
		mObserver = observer;
	}

	@Override
	public void execute(final Runnable command) {

		final PrioritizableRequest request = (PrioritizableRequest) command;
		final Identifier<?> identifier = request.getIdentifier();

		if (identifier == null) {
			throw new IllegalStateException("Identifier cannot be null.");
		}

		if (!mIdentifiers.contains(identifier)) {
			mIdentifiers.add(identifier);

			// Run synchronously
			request.run();

			// Notify immediately
			if (request.isCancelled()) {
				mObserver.onCancelled(request);
			} else {
				mObserver.onComplete(request);
			}
		}
	}

	@Override
	public void notifyRequestComplete(final Identifier<?> identifier) {
		mIdentifiers.remove(identifier);
	}

	@Override
	public BlockingQueue<Runnable> getQueue() {
		return new ArrayBlockingQueue<Runnable>(1);
	}

	@Override
	public int getActiveCount() {
		return mIdentifiers.size();
	}

	@Override
	public boolean remove(final Runnable task) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancelAll() {
		throw new UnsupportedOperationException();
	}

}
