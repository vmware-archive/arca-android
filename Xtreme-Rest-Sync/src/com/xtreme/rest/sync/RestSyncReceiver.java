package com.xtreme.rest.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStats;

import com.xtreme.rest.broadcasts.RestBroadcastReceiver;
import com.xtreme.rest.broadcasts.RestBroadcaster;

public class RestSyncReceiver extends RestBroadcastReceiver {

	private final RestSyncListener mListener;
	
	public RestSyncReceiver(final RestSyncListener listener) {
		mListener = listener;
	}
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
		final SyncStats stats = RestBroadcaster.getStats(intent);
		if (mListener != null && stats != null) {
			mListener.onSyncComplete(stats);
		}
	}

}