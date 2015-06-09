/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
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
