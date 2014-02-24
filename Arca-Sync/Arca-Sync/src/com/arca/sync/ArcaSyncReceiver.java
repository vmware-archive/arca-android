package com.arca.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStats;

import com.arca.broadcasts.ArcaBroadcastReceiver;

public class ArcaSyncReceiver extends ArcaBroadcastReceiver {

	private final ArcaSyncListener mListener;
	
	public ArcaSyncReceiver(final ArcaSyncListener listener) {
		mListener = listener;
	}
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
		final SyncStats stats = ArcaSyncBroadcaster.getSyncStats(intent);
		if (mListener != null && stats != null) {
			mListener.onSyncComplete(stats);
		}
	}

}