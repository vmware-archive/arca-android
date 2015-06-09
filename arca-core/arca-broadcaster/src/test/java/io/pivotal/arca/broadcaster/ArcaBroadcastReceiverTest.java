/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
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
