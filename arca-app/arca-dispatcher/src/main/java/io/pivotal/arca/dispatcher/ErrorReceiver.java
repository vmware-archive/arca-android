/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import io.pivotal.arca.broadcaster.ArcaBroadcastReceiver;

public class ErrorReceiver extends ArcaBroadcastReceiver {

	private final ErrorListener mListener;

	public ErrorReceiver(final ErrorListener listener) {
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
