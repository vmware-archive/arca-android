package com.xtreme.rest.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStats;
import android.net.Uri;

import com.xtreme.rest.broadcasts.RestBroadcastManager;

public class RestSyncBroadcaster {

	
	private static final class Extras {
		private static final String SYNC_STATS = "sync_stats";
	}
	
	public static void broadcast(final Context context, final Uri uri, final SyncStats stats) {
		final Intent intent = buildSyncStatsIntent(uri, stats);
		RestBroadcastManager.sendBroadcast(context, intent);
	}

	private static Intent buildSyncStatsIntent(final Uri uri, final SyncStats stats) {
		final Intent intent = new Intent(uri.toString());
		intent.putExtra(Extras.SYNC_STATS, stats);
		return intent;
	}
	
	
	// ===============================

	
	public static SyncStats getSyncStats(final Intent intent) {
		if (intent != null) {
			return (SyncStats) intent.getParcelableExtra(Extras.SYNC_STATS);
		} else {
			return null;
		}
	}
	
}