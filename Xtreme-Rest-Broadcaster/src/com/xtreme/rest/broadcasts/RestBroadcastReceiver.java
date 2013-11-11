package com.xtreme.rest.broadcasts;

import android.content.BroadcastReceiver;

public abstract class RestBroadcastReceiver extends BroadcastReceiver {
	
	private volatile boolean mIsRegistered = false;

	public synchronized void register(final String action) {
		if (!mIsRegistered) {
			RestBroadcastManager.registerReceiver(this, action);
			mIsRegistered = true;
		}
	}

	public synchronized void unregister() {
		if (mIsRegistered) {
			RestBroadcastManager.unregisterReceiver(this);
			mIsRegistered = false;
		}
	}

	protected synchronized boolean isRegistered() {
		return mIsRegistered;
	}
}