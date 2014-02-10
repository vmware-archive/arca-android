package com.xtreme.rest.broadcaster.test;

import com.xtreme.rest.broadcasts.RestBroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;

public class RestBroadcastReceiverTest extends AndroidTestCase {

	private static final String ACTION = "action";
	
	
	public void testReceiverNotRegistered() {
		final RestBroadcastReceiver receiver = new TestRestBroadcastReceiver();
		
		assertFalse(receiver.isRegistered());
	}
	
	public void testReceiverRegistered() {
		final RestBroadcastReceiver receiver = new TestRestBroadcastReceiver();
		receiver.register(ACTION);
		
		assertTrue(receiver.isRegistered());
	}
	
	public void testReceiverUnRegistered() {
		final RestBroadcastReceiver receiver = new TestRestBroadcastReceiver();
		receiver.register(ACTION);
		receiver.unregister();
		
		assertFalse(receiver.isRegistered());
	}
	
	
	// ==========================================
	
	
	
	private static final class TestRestBroadcastReceiver extends RestBroadcastReceiver {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			// do nothing
		}
	}
	
}
