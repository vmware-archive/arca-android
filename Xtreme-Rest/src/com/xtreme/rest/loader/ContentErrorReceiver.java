package com.xtreme.rest.loader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.xtreme.rest.broadcasts.RestBroadcastReceiver;
import com.xtreme.rest.broadcasts.RestBroadcaster;
import com.xtreme.rest.broadcasts.RestError;

class ContentErrorReceiver extends RestBroadcastReceiver {
	
	private final ContentErrorListener mListener;
	
	public ContentErrorReceiver(final ContentErrorListener listener) {
		super();
		mListener = listener;
	}
	
	public void register(final Uri uri) {
		if (uri != null) {
			register(uri.toString());
		}
	}

	public void update(final Uri uri) {
		unregister();
		register(uri);
	}
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
		final RestError error = RestBroadcaster.getError(intent);
		if (mListener != null && error != null) {
			mListener.onError(error);
		}
	}
}
