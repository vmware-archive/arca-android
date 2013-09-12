package com.xtreme.rest.service.test.junit.tests;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

import com.xtreme.rest.service.RestService;

public class RestServiceTest extends ServiceTestCase<RestService> {

	public RestServiceTest() {
		super(RestService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
    public void testStartable() {
        final Intent startIntent = new Intent();
        startIntent.setClass(getContext(), RestService.class);
        startService(startIntent);
    }

    public void testNotBindable() {
        final Intent startIntent = new Intent();
        startIntent.setClass(getContext(), RestService.class);
        final IBinder service = bindService(startIntent);
        assertNull(service);
    }
    
}
