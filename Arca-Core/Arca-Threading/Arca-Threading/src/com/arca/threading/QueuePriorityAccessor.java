package com.arca.threading;

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
