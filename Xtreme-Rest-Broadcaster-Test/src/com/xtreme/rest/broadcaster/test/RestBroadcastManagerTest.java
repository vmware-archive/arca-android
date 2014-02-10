package com.xtreme.rest.broadcaster.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.test.AndroidTestCase;

import com.xtreme.rest.broadcasts.RestBroadcastManager;
import com.xtreme.rest.broadcasts.RestBroadcastManager.BroadcastHandler;

public class RestBroadcastManagerTest extends AndroidTestCase {

	private static final String ACTION = "action";

	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final Context context = getContext();
		final Looper looper = context.getMainLooper();
		final TestBroadcastHandler handler = new TestBroadcastHandler(looper);
		
		RestBroadcastManager.initializeHandler(context, handler);
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
		
		RestBroadcastManager.registerReceiver(receiver, ACTION);
		RestBroadcastManager.sendBroadcast(getContext(), new Intent(ACTION));
		RestBroadcastManager.unregisterReceiver(receiver);
		
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
		
		RestBroadcastManager.registerReceiver(receiver1, ACTION);
		RestBroadcastManager.registerReceiver(receiver2, ACTION);
		
		RestBroadcastManager.sendBroadcast(getContext(), new Intent(ACTION));
		
		RestBroadcastManager.unregisterReceiver(receiver1);
		RestBroadcastManager.unregisterReceiver(receiver2);
		
		latch.assertComplete();
	}
	
	
	// ============================================
	
	
	public static class TestBroadcastHandler extends BroadcastHandler {

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
