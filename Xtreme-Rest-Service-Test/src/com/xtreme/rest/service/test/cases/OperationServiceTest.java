package com.xtreme.rest.service.test.cases;

import android.content.Intent;
import android.os.IBinder;
import android.test.ServiceTestCase;

import com.xtreme.rest.service.OperationService;

public class OperationServiceTest extends ServiceTestCase<OperationService> {

	public OperationServiceTest() {
		super(OperationService.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testOperationServiceActionStart() {
		assertEquals(OperationService.Action.START, OperationService.Action.valueOf("START"));
	}
	
	public void testOperationServiceActionCancel() {
		assertEquals(OperationService.Action.CANCEL, OperationService.Action.valueOf("CANCEL"));
	}
	
	public void testOperationServiceActionValues() {
		final OperationService.Action[] actions = OperationService.Action.values();
		assertEquals(OperationService.Action.START, actions[0]);
		assertEquals(OperationService.Action.CANCEL, actions[1]);
	}
	
	
	// ===========================================

	
	public void testOperationServiceStartable() {
    	final Intent intent = new Intent(getContext(), OperationService.class);
		startService(intent);
	}
	
    public void testOperationServiceNotBindable() {
        final Intent intent = new Intent(getContext(), OperationService.class);
		final IBinder binder = bindService(intent);
        assertNull(binder);
    }
    
    
    // ===========================================
    
    
    public void testOperationServiceStartWithoutContext() {
    	assertFalse(OperationService.start(null, null));
    }
    
    public void testOperationServiceCancelWithoutContext() {
    	assertFalse(OperationService.cancel(null, null));
    }
    
    public void testOperationServiceStartWithoutOperation() {
    	assertFalse(OperationService.start(getContext(), null));
    }
    
    public void testOperationServiceCancelWithoutOperation() {
    	assertFalse(OperationService.cancel(getContext(), null));
    }
	
}
