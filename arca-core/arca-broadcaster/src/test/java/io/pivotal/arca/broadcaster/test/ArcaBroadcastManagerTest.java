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
package io.pivotal.arca.broadcaster.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.test.AndroidTestCase;

import io.pivotal.arca.broadcaster.ArcaBroadcastManager;

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
