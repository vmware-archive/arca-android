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

	public void testRestServiceActionStart() {
		assertEquals(RestService.Action.START, RestService.Action.valueOf("START"));
	}
	
	public void testRestServiceActionCancel() {
		assertEquals(RestService.Action.CANCEL, RestService.Action.valueOf("CANCEL"));
	}
	
	public void testRestServiceActionValues() {
		final RestService.Action[] actions = RestService.Action.values();
		assertEquals(RestService.Action.START, actions[0]);
		assertEquals(RestService.Action.CANCEL, actions[1]);
	}
	
	
	// ===========================================

	
	public void testRestServiceStartable() {
    	final Intent intent = new Intent(getContext(), RestService.class);
		startService(intent);
	}
	
    public void testRestServiceNotBindable() {
        final Intent intent = new Intent(getContext(), RestService.class);
		final IBinder service = bindService(intent);
        assertNull(service);
    }
    
    
    // ===========================================
    
    
    public void testRestServiceStartWithoutContext() {
    	assertFalse(RestService.start(null, null));
    }
    
    public void testRestServiceCancelWithoutContext() {
    	assertFalse(RestService.cancel(null, null));
    }
    
    public void testRestServiceStartWithoutOperation() {
    	assertFalse(RestService.start(getContext(), null));
    }
    
    public void testRestServiceCancelWithoutOperation() {
    	assertFalse(RestService.cancel(getContext(), null));
    }
	
}
