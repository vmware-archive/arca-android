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
