package com.xtreme.rest.dispatcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.xtreme.rest.broadcasts.RestBroadcastReceiver;

public class ErrorReceiver extends RestBroadcastReceiver {
	
	private final ErrorListener mListener;
	
	public ErrorReceiver(final ErrorListener listener) {
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
		final Error error = ErrorBroadcaster.getError(intent);
		if (mListener != null && error != null) {
			mListener.onRequestError(error);
		}
	}
}
