package io.pivotal.arca.threading;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AuxiliaryBlockingQueue extends AbstractQueue<Runnable> implements BlockingQueue<Runnable> {

	private final AuxiliaryQueue mQueue;
	private final ReentrantLock mLock = new ReentrantLock(true);
	private final Condition mNotEmpty;
	private int mCount = 0;

	public AuxiliaryBlockingQueue(final PriorityAccessor[] accessors, final AuxiliaryExecutorObserver observer) {
		mQueue = new AuxiliaryQueue(accessors, observer);
		mNotEmpty = mLock.newCondition();
	}

	@Override
	public boolean offer(final Runnable e) {
		checkNotNull(e);
		mLock.lock();
		try {
			insert(e);
			return true;
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public Runnable peek() {
		mLock.lock();
		try {
			return mQueue.peek();
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public Runnable poll() {
		mLock.lock();
		try {
			return extract();
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public void clear() {
		mLock.lock();
		try {
			while (extract() != null)
				;
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public Iterator<Runnable> iterator() {
		return null;
	}

	@Override
	public int size() {
		mLock.lock();
		try {
			return mQueue.size();
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public int drainTo(final Collection<? super Runnable> collection) {
		checkNotNull(collection);
		mLock.lock();
		try {
			int numDrained = 0;
			while (mQueue.size() > 0) {
				final Runnable runnable = extract();
				if (runnable == null) {
					break;
				}
				collection.add(runnable);
				numDrained++;
			}
			// FIXME We need to signal an empty queue here.
			return numDrained;
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public int drainTo(final Collection<? super Runnable> collection, final int maxNumberToDrain) {
		checkNotNull(collection);
		mLock.lock();
		try {
			int numDrained = 0;
			for (int i = 0; i < maxNumberToDrain; i++) {
				final Runnable runnable = extract();
				if (runnable == null) {
					break;
				}
				collection.add(runnable);
				numDrained++;
			}
			return numDrained;
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public boolean remove(final Object object) {
		return false; // TODO need to implement
	}

	@Override
	public boolean removeAll(final Collection<?> collection) {
		return false; // TODO need to implement
	}

	@Override
	public boolean offer(final Runnable e, final long timeout, final TimeUnit unit) throws InterruptedException {
		checkNotNull(e);
		mLock.lockInterruptibly();
		try {
			insert(e);
			return true;
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public Runnable poll(final long timeout, final TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		mLock.lockInterruptibly();
		try {
			Runnable runnable = extract();
			while (runnable == null) {
				if (nanos <= 0) {
					return null;
				}
				nanos = mNotEmpty.awaitNanos(nanos);
				runnable = extract();
			}
			return runnable;
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public void put(final Runnable e) throws InterruptedException {
		checkNotNull(e);
		mLock.lockInterruptibly();
		try {
			insert(e);
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public int remainingCapacity() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Runnable take() throws InterruptedException {
		mLock.lockInterruptibly();
		try {
			Runnable runnable = extract();
			while (runnable == null) {
				mNotEmpty.await();
				runnable = extract();
			}
			return runnable;
		} finally {
			mLock.unlock();
		}
	}

	public void runInBlockingQueueLock(final Runnable runnable) {
		mLock.lock();
		try {
			runnable.run();
		} finally {
			mLock.unlock();
		}
	}

	private void insert(final Runnable r) {
		mQueue.add((PrioritizableRequest) r);
		mCount++;
		if (mCount == 1) {
			mNotEmpty.signal();
		}
	}

	private PrioritizableRequest extract() {
		PrioritizableRequest request;
		do {
			request = mQueue.removeHighestPriorityRunnable();
			if (mCount > 0) {
				if (request != null) {
					mCount--;
				} else {
					mCount = mQueue.size();
				}
			}
		} while ((request == null || request.isCancelled()) && mCount > 0);

		if (request == null) {
			return null;
		} else {
			return request.isCancelled() ? null : request;
		}
	}

	private static void checkNotNull(final Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
	}

}
