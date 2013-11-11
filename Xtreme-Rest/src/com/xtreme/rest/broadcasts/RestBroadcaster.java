package com.xtreme.rest.broadcasts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class RestBroadcaster {

	
	private static final class Extras {
		private static final String ERROR = "error";
	}
	
	public static void broadcast(final Context context, final Uri uri, final int errorCode, final String errorMessage) {
		final Intent intent = buildErrorIntent(uri, errorCode, errorMessage);
		RestBroadcastManager.sendBroadcast(context, intent);
	}

	private static Intent buildErrorIntent(final Uri uri, final int code, final String message) {
		final Intent intent = new Intent(uri.toString());
		intent.putExtra(Extras.ERROR, new RestError(code, message));
		return intent;
	}
	
	
	// ===============================

	
	public static RestError getError(final Intent intent) {
		if (intent != null) {
			return (RestError) intent.getParcelableExtra(Extras.ERROR);
		} else {
			return null;
		}
	}
	
}