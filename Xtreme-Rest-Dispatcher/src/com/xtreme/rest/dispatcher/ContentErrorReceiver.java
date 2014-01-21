package com.xtreme.rest.dispatcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.xtreme.rest.broadcasts.RestBroadcastReceiver;

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
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
		final ContentError error = ContentBroadcaster.getError(intent);
		if (mListener != null && error != null) {
			mListener.onRequestError(error);
		}
	}
}
