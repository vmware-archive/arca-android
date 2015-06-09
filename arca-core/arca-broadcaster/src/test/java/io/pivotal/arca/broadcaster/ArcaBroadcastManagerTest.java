/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ArcaBroadcastManagerTest extends AndroidTestCase {

	private static final String ACTION = "action";

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final Looper looper = getContext().getMainLooper();
		final TestBroadcastHandler handler = new TestBroadcastHandler(looper);

		ArcaBroadcastManager.initialize(getContext(), handler);
	}

	public void testSendBroadcast() {

		final AssertionLatch latch = new AssertionLatch(1);
		final BroadcastReceiver receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(final Context c, final Intent intent) {
				latch.countDown();
				assertEquals(ACTION, intent.getAction());
			}
		};

		ArcaBroadcastManager.registerReceiver(receiver, ACTION);
		ArcaBroadcastManager.sendBroadcast(getContext(), new Intent(ACTION));
		ArcaBroadcastManager.unregisterReceiver(receiver);

		latch.assertComplete();
	}

	public void testSendBroadcastToMultipleReceivers() {

		final AssertionLatch latch = new AssertionLatch(2);
		final BroadcastReceiver receiver1 = new BroadcastReceiver() {

			@Override
			public void onReceive(final Context c, final Intent intent) {
				latch.countDown();
				assertEquals(ACTION, intent.getAction());
			}
		};

		final BroadcastReceiver receiver2 = new BroadcastReceiver() {

			@Override
			public void onReceive(final Context c, final Intent intent) {
				latch.countDown();
				assertEquals(ACTION, intent.getAction());
			}
		};

		ArcaBroadcastManager.registerReceiver(receiver1, ACTION);
		ArcaBroadcastManager.registerReceiver(receiver2, ACTION);

		ArcaBroadcastManager.sendBroadcast(getContext(), new Intent(ACTION));

		ArcaBroadcastManager.unregisterReceiver(receiver1);
		ArcaBroadcastManager.unregisterReceiver(receiver2);

		latch.assertComplete();
	}

	// ============================================

	public static class TestBroadcastHandler extends ArcaBroadcastManager.BroadcastHandler {

		public TestBroadcastHandler(Looper looper) {
			super(looper);
		}

		@Override
		public boolean sendMessageAtTime(final Message msg, final long uptimeMillis) {
			handleMessage(msg);
			return true;
		}
	}

	// ============================================

	public class AssertionLatch extends CountDownLatch {

		public AssertionLatch(final int count) {
			super(count);
		}

		@Override
		public void countDown() {
			final long count = getCount();
			if (count == 0) {
				Assert.fail("This latch has already finished.");
			} else {
				super.countDown();
			}
		}

		public void assertComplete() {
			try {
				Assert.assertTrue(await(0, TimeUnit.SECONDS));
			} catch (final InterruptedException e) {
				Assert.fail();
			}
		}
	}

}
