package com.xtreme.rest.broadcasts;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStats;
import android.net.Uri;

public class RestBroadcaster {

	/*
	 * TODO this should be separated into two different broadcasters 
	 * and packaged independently with the sync and service projects.
	 * 
	 */
	
	private static final class Extras {
		private static final String ERROR = "error";
		private static final String STATS = "stats";
	}
	
	
	public static void broadcast(final Context context, final Uri uri, final int errorCode, final String errorMessage) {
		final Intent intent = buildErrorIntent(uri, errorCode, errorMessage);
		RestBroadcastManager.sendBroadcast(context, intent);
	}
	
	public static void broadcast(final Context context, final Uri uri, final SyncStats stats) {
		final Intent intent = buildSyncStatsIntent(uri, stats);
		RestBroadcastManager.sendBroadcast(context, intent);
	}
	
	
	// ===============================
	

	private static Intent buildErrorIntent(final Uri uri, final int code, final String message) {
		final Intent intent = new Intent(uri.toString());
		intent.putExtra(Extras.ERROR, new RestError(code, message));
		return intent;
	}

	private static Intent buildSyncStatsIntent(final Uri uri, final SyncStats stats) {
		final Intent intent = new Intent(uri.toString());
		intent.putExtra(Extras.STATS, stats);
		return intent;
	}
	
	
	// ===============================
	
	
	public static SyncStats getStats(final Intent intent) {
		if (intent != null) {
			return (SyncStats) intent.getParcelableExtra(Extras.STATS);
		} else {
			return null;
		}
	}
	
	public static RestError getError(final Intent intent) {
		if (intent != null) {
			return (RestError) intent.getParcelableExtra(Extras.ERROR);
		} else {
			return null;
		}
	}
	
}