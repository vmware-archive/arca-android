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
package com.arca.threading;

import java.util.Iterator;

class AuxiliaryQueue {

	private final PriorityAccessor[] mPriorityAccessors;
	private final AuxiliaryExecutorObserver mObserver;
	private final int mNumAccessors;

	public AuxiliaryQueue(final PriorityAccessor[] accessors, final AuxiliaryExecutorObserver observer) {
		mNumAccessors = accessors.length;
		mPriorityAccessors = new PriorityAccessor[mNumAccessors];
		mObserver = observer;

		for (int i = 0; i < mNumAccessors; i++) {
			if (accessors[i] == null) {
				throw new IllegalArgumentException("The accessor provided at index " + i + " is null!");
			}
			mPriorityAccessors[i] = accessors[i];
		}
	}

	public synchronized void add(final PrioritizableRequest request) {
		final int index = request.getAccessorIndex();
		mPriorityAccessors[index].attach(request);
	}

	public synchronized PrioritizableRequest removeHighestPriorityRunnable() {
		PrioritizableRequest request;
		for (int i = 0; i < mNumAccessors; i++) {
			while ((request = mPriorityAccessors[i].detachHighestPriorityItem()) != null) {
				if (!request.isCancelled()) {
					return request;
				} else {
					mObserver.onCancelled(request);
				}
			}
		}
		return null;
	}

	public synchronized int size() {
		int size = 0;
		for (int i = 0; i < mNumAccessors; i++) {
			size += mPriorityAccessors[i].size();
		}
		return size;
	}

	public synchronized Runnable peek() {
		PrioritizableRequest request;
		for (int i = 0; i < mNumAccessors; i++) {
			if ((request = mPriorityAccessors[i].peek()) != null) {
				return request;
			}
		}
		return null;
	}

	// TODO Fill in the iterator.
	public Iterator<Runnable> buildIterator() {
		return new Iterator<Runnable>() {
			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public Runnable next() {
				return null;
			}

			@Override
			public void remove() {
			}
		};
	}

	// TODO Fill in the "buildArray" method.
	public Object[] buildArray() {
		return null;
	}

	public void clear() {
		for (int i = 0; i < mNumAccessors; i++) {
			mPriorityAccessors[i].clear();
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public interface OnRemovedListener {
		public void onRemoved();

		public void onRemovalFailed();
	}

}
