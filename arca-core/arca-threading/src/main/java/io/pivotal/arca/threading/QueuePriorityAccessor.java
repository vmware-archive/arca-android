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

public class QueuePriorityAccessor implements PriorityAccessor {
	private final HashedQueue<PrioritizableRequest> mQueue = new HashedQueue<PrioritizableRequest>();

	@Override
	public void attach(final PrioritizableRequest request) {
		mQueue.add(request);
	}

	@Override
	public PrioritizableRequest detachHighestPriorityItem() {
		return mQueue.poll();
	}

	@Override
	public int size() {
		return mQueue.size();
	}

	@Override
	public PrioritizableRequest peek() {
		return mQueue.peek();
	}

	@Override
	public void clear() {
		mQueue.clear();
	}
}
