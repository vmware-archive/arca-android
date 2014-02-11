package com.xtreme.rest.dispatcher.test;

import junit.framework.Assert;

import com.xtreme.rest.dispatcher.Request;

import android.net.Uri;
import android.test.AndroidTestCase;

public class RequestTest extends AndroidTestCase {

	public void testRequestWithNullUriThrowsException() {
		try {
			new TestRequest(null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}
	}
	
	public void testRequestWithNullUriAndIdentifierThrowsException() {
		try {
			new TestRequest(null, -1);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}
	}
	
	public void testRequestWithIdentifierLessThan1000ThrowsException() {
		try {
			new TestRequest(Uri.EMPTY, 999);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}
	}
	
	public void testRequestWithIdentifierof1000orMoreSucceeds() {
		assertNotNull(new TestRequest(Uri.EMPTY, 1000));
	}
	
	public void testRequestWithDefaultIdentifierSucceeds() {
		assertNotNull(new TestRequest(Uri.EMPTY));
	}
	
	private static final class TestRequest extends Request<Object> {

		public TestRequest(final Uri uri) {
			super(uri);
		}

		public TestRequest(final Uri uri, final int identifier) {
			super(uri, identifier);
		}
		
	}
	
}
