package com.rottentomatoes.app.providers;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;

public class RottenTomatoesUriCache {

	private static final int EXPIRATION_MS = 30 * 1000;
	private static final Map<Uri, Long> CACHE = new HashMap<Uri, Long>();
	
	public static synchronized void add(final Uri uri) {
		CACHE.put(uri, System.currentTimeMillis());
	}
	
	public static synchronized boolean isExpired(final Uri uri) {
		final Long timestamp = CACHE.get(uri);
		if (timestamp != null) { 
			final long current = System.currentTimeMillis();
			return (timestamp + EXPIRATION_MS) < current;
		} else {
			return true;
		}
	}
	
}
