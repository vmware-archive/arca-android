package com.xtreme.rest.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.SyncStats;
import android.net.Uri;

public class RestSynchronizer implements RestSyncListener {
	
	private static final int DEFAULT_TIMEOUT = 60;

	private final RestSyncReceiver mReceiver;
	private final int mTimeout;
	
	private CountDownLatch mLatch;
	private SyncStats mStats;
	
	public RestSynchronizer() {
		this(DEFAULT_TIMEOUT);
	}
	
	public RestSynchronizer(final int timeout) {
		mReceiver = new RestSyncReceiver(this);
		mTimeout = timeout;
	}

	public SyncStats awaitStatsBroadcast(final Uri uri) throws InterruptedException {
		if (uri == null) return null;
		
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