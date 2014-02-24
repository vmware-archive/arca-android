package com.arca.broadcasts;

import android.content.BroadcastReceiver;

public abstract class ArcaBroadcastReceiver extends BroadcastReceiver {
	
	private volatile boolean mIsRegistered = false;

	public synchronized void register(final String action) {
		if (!mIsRegistered) {
			ArcaBroadcastManager.registerReceiver(this, action);
			mIsRegistered = true;
		}
	}

	public synchronized void unregister() {
		if (mIsRegistered) {
			ArcaBroadcastManager.unregisterReceiver(this);
			mIsRegistered = false;
		}
	}

	public synchronized boolean isRegistered() {
		return mIsRegistered;
	}
}