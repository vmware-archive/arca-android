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

import android.content.Context;
import android.content.Intent;
import android.content.SyncStats;
import android.net.Uri;

import com.arca.broadcasts.ArcaBroadcastManager;

public class ArcaSyncBroadcaster {

	private static interface Extras {
		public static final String SYNC_STATS = "sync_stats";
	}

	public static void broadcast(final Context context, final Uri uri, final SyncStats stats) {
		final Intent intent = buildSyncStatsIntent(uri, stats);
		ArcaBroadcastManager.sendBroadcast(context, intent);
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