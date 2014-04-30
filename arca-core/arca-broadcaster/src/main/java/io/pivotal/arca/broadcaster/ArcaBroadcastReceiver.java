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
package io.pivotal.arca.broadcaster;

import android.content.BroadcastReceiver;

public abstract class ArcaBroadcastReceiver extends BroadcastReceiver {

	private volatile boolean mIsRegistered = false;

	public synchronized void register(final String action) {
		if (!mIsRegistered) {
			ArcaBroadcastManager.registerReceiver(this, action);
			mIsRegistered = true;
		}
	}

	public synchronized void unregister() {
		if (mIsRegistered) {
			ArcaBroadcastManager.unregisterReceiver(this);
			mIsRegistered = false;
		}
	}

	public synchronized boolean isRegistered() {
		return mIsRegistered;
	}
}