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

import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;

import io.pivotal.arca.broadcaster.ArcaBroadcastReceiver;

public class ArcaBroadcastReceiverTest extends AndroidTestCase {

	private static final String ACTION = "action";

	public void testReceiverNotRegistered() {
		final ArcaBroadcastReceiver receiver = new TestArcaBroadcastReceiver();

		assertFalse(receiver.isRegistered());
	}

	public void testReceiverRegistered() {
		final ArcaBroadcastReceiver receiver = new TestArcaBroadcastReceiver();
		receiver.register(ACTION);

		assertTrue(receiver.isRegistered());
	}

	public void testReceiverUnRegistered() {
		final ArcaBroadcastReceiver receiver = new TestArcaBroadcastReceiver();
		receiver.register(ACTION);
		receiver.unregister();

		assertFalse(receiver.isRegistered());
	}

	// ==========================================

	private static final class TestArcaBroadcastReceiver extends ArcaBroadcastReceiver {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			// do nothing
		}
	}

}
