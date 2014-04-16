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
