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
package com.arca.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.SyncStats;
import android.net.Uri;

public class ArcaSynchronizer implements ArcaSyncListener {

	private static final int DEFAULT_TIMEOUT = 60;

	private final ArcaSyncReceiver mReceiver;
	private final int mTimeout;

	private CountDownLatch mLatch;
	private SyncStats mStats;

	public ArcaSynchronizer() {
		this(DEFAULT_TIMEOUT);
	}

	public ArcaSynchronizer(final int timeout) {
		mReceiver = new ArcaSyncReceiver(this);
		mTimeout = timeout;
	}

	public SyncStats awaitStatsBroadcast(final Uri uri) throws InterruptedException {
		if (uri == null)
			return null;

		mLatch = new CountDownLatch(1);
		mReceiver.register(uri.toString());

		try {
			mLatch.await(mTimeout, TimeUnit.SECONDS);
			return getStats();
		} finally {
			mReceiver.unregister();
		}
	}

	private SyncStats getStats() {
		if (mStats != null) {
			return mStats;
		} else {
			return failureStats();
		}
	}

	private static SyncStats failureStats() {
		final SyncStats stats = new SyncStats();
		stats.numIoExceptions++;
		return stats;
	}

	@Override
	public void onSyncComplete(final SyncStats stats) {
		mStats = stats;
		mLatch.countDown();
	}

}