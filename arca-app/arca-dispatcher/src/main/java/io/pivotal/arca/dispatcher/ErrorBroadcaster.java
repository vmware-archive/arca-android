/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.dispatcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import io.pivotal.arca.broadcaster.ArcaBroadcastManager;

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