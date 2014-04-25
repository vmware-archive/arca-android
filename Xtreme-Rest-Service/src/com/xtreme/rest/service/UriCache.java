package com.xtreme.rest.service;

import android.net.Uri;
import android.support.v4.util.LruCache;

class UriCache {
	private static final long TIMEOUT_IN_MILLIS = 1000;
	private static final int URI_TIMESTAMP_CACHE_MAXIMUM_SIZE = 50;

	private final LruCache<Uri, Long> mUriCache = new LruCache<Uri, Long>(URI_TIMESTAMP_CACHE_MAXIMUM_SIZE);

	public synchronized boolean shouldStart(final Uri uri) {
		Long timestamp = mUriCache.get(uri);
		timestamp = timestamp == null ? 0 : timestamp;
		return System.currentTimeMillis() - timestamp >= TIMEOUT_IN_MILLIS;
	}

	public synchronized void markComplete(final Uri uri) {
		mUriCache.put(uri, System.currentTimeMillis());
	}
}
