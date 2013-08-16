package com.xtreme.rest.service.test.junit.tests;

import android.net.Uri;
import android.test.AndroidTestCase;

import com.xtreme.rest.service.test.junit.mock.TestOperation;
import com.xtreme.rest.service.test.junit.mock.TestOperationObserver;
import com.xtreme.rest.service.test.junit.mock.TestRequestHandler;

public class OperationTest extends AndroidTestCase {

	private TestOperation mOperation;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		final Uri uri = Uri.parse("content://test");
		
		mOperation = new TestOperation(uri);
		mOperation.setOperationObserver(new TestOperationObserver());
		mOperation.setRequestHandler(new TestRequestHandler());
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testInitialization() {
		// success
	}
}
