/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.threading;

public class StackPriorityAccessor implements PriorityAccessor {
	private final HashedStack<PrioritizableRequest> mStack = new HashedStack<PrioritizableRequest>();

	@Override
	public synchronized PrioritizableRequest detachHighestPriorityItem() {
		return mStack.pop();
	}

	@Override
	public synchronized void attach(final PrioritizableRequest request) {
		mStack.push(request);
	}

	@Override
	public synchronized int size() {
		return mStack.size();
	}

	@Override
	public PrioritizableRequest peek() {
		return mStack.peek();
	}

	@Override
	public void clear() {
		mStack.clear();
	}
}
