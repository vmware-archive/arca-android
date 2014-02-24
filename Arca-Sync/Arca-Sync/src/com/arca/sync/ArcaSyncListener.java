package com.arca.sync;

import android.content.SyncStats;

public interface ArcaSyncListener {
	public void onSyncComplete(SyncStats stats);
}
