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
package com.arca.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStats;

import com.arca.broadcasts.ArcaBroadcastReceiver;

public class ArcaSyncReceiver extends ArcaBroadcastReceiver {

	private final ArcaSyncListener mListener;

	public ArcaSyncReceiver(final ArcaSyncListener listener) {
		mListener = listener;
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		final SyncStats stats = ArcaSyncBroadcaster.getSyncStats(intent);
		if (mListener != null && stats != null) {
			mListener.onSyncComplete(stats);
		}
	}

}