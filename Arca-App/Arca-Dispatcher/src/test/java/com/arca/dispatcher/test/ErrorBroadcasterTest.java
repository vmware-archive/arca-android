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
package com.arca.dispatcher.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.os.Message;
import android.test.AndroidTestCase;

import com.arca.broadcaster.ArcaBroadcastManager;
import com.arca.broadcaster.ArcaBroadcastManager.BroadcastHandler;
import com.arca.dispatcher.Error;
import com.arca.dispatcher.ErrorBroadcaster;
import com.arca.dispatcher.ErrorListener;
import com.arca.dispatcher.ErrorReceiver;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ErrorBroadcasterTest extends AndroidTestCase {

	private static final Uri URI = Uri.parse("content://empty");

	private static final int ERROR_CODE = 100;
	private static final String ERROR_MESSAGE = "error message";

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final Looper looper = getContext().getMainLooper();
		final TestBroadcastHandler handler = new TestBroadcastHandler(looper);

		ArcaBroadcastManager.initialize(getContext(), handler);
	}

	public void testErrorBroadcast() {
		final AssertionLatch latch = new AssertionLatch(1);
		final ErrorReceiver receiver = new ErrorReceiver(new ErrorListener() {

			@Override
			public void onRequestError(final Error error) {
				latch.countDown();

				assertEquals(ERROR_CODE, error.getCode());
				assertEquals(ERROR_MESSAGE, error.getMessage());
			}
		});
		receiver.register(URI);
		ErrorBroadcaster.broadcast(getContext(), URI, ERROR_CODE, ERROR_MESSAGE);
		latch.assertComplete();
	}

	public void testErrorFromIntent() {
		final Error error = new Error(ERROR_CODE, ERROR_MESSAGE);
		final Intent intent = new Intent(URI.toString());
		intent.putExtra(ErrorBroadcaster.Extras.ERROR, error);
		final Error actual = ErrorBroadcaster.getError(intent);
		assertEquals(error, actual);
	}

	public void testNullErrorFromIntent() {
		final Error error = ErrorBroadcaster.getError(null);
		assertNull(error);
	}

	// ============================================

	private static class TestBroadcastHandler extends BroadcastHandler {

		public TestBroadcastHandler(final Looper looper) {
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
