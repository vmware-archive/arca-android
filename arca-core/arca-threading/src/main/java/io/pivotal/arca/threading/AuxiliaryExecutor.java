/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.threading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public interface AuxiliaryExecutor {

	public void notifyRequestComplete(final Identifier<?> identifier);

	public BlockingQueue<Runnable> getQueue();

	public int getActiveCount();

	public void execute(Runnable command);

	public boolean remove(Runnable task);

	public void cancelAll();

	public static class Builder {

		private int mCorePoolSize = 1;
		private int mAdditionalThreads = 0;
		private long mKeepAliveTime = 0;
		private TimeUnit mTimeUnit = TimeUnit.MILLISECONDS;
		private final PriorityAccessor[] mPriorityAccessors;
		private final AuxiliaryExecutorObserver mObserver;
		private boolean mAllowCoreThreadTimeOut;

		public Builder(final PriorityAccessor[] accessors, final AuxiliaryExecutorObserver observer) {
			if (accessors == null)
				throw new IllegalArgumentException("Accessor array cannot be null.");

			if (observer == null)
				throw new IllegalArgumentException("Executor observer cannot be null.");

			mPriorityAccessors = accessors;
			mObserver = observer;
		}

		public Builder setCorePoolSize(final int corePoolSize) {
			mCorePoolSize = corePoolSize;
			return this;
		}

		public Builder setNumAdditionalThreads(final int additionalThreads) {
			mAdditionalThreads = additionalThreads;
			return this;
		}

		public Builder setKeepAliveTime(final long keepAliveTime, final TimeUnit timeUnit) {
			mKeepAliveTime = keepAliveTime;
			mTimeUnit = timeUnit;
			return this;
		}

		public Builder allowCoreThreadTimeOut() {
			mAllowCoreThreadTimeOut = true;
			return this;
		}

		public AuxiliaryExecutor create() {
			final AuxiliaryBlockingQueue queue = new AuxiliaryBlockingQueue(mPriorityAccessors, mObserver);
			final DefaultAuxiliaryExecutor executor = new DefaultAuxiliaryExecutor(mCorePoolSize, mCorePoolSize + mAdditionalThreads, mKeepAliveTime, mTimeUnit, queue, mObserver);
			executor.allowCoreThreadTimeOut(mAllowCoreThreadTimeOut);
			return executor;
		}
	}
}
