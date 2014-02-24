package com.arca.dispatcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.arca.broadcasts.ArcaBroadcastManager;

public class ErrorBroadcaster {

	public static interface Extras {
		public static final String ERROR = "error";
	}
	
	public static void broadcast(final Context context, final Uri uri, final int errorCode, final String errorMessage) {
		final Intent intent = buildErrorIntent(uri, errorCode, errorMessage);
		ArcaBroadcastManager.sendBroadcast(context, intent);
	}

	private static Intent buildErrorIntent(final Uri uri, final int code, final String message) {
		final Intent intent = new Intent(uri.toString());
		intent.putExtra(Extras.ERROR, new Error(code, message));
		return intent;
	}
	
	
	// ===============================

	
	public static Error getError(final Intent intent) {
		if (intent != null) {
			return (Error) intent.getParcelableExtra(Extras.ERROR);
		} else {
			return null;
		}
	}
	
}