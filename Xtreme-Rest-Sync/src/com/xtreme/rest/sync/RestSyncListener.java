package com.xtreme.rest.sync;

import android.content.SyncStats;

public interface RestSyncListener {
	public void onSyncComplete(SyncStats stats);
}
