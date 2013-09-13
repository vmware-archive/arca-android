package com.xtreme.rest.service.test.cases;

import android.content.ComponentName;
import android.content.ContextWrapper;
import android.content.Intent;
import android.test.ServiceTestCase;

import com.xtreme.rest.service.RestService;
import com.xtreme.rest.service.RestService.Action;
import com.xtreme.rest.service.test.mock.TestOperation;
import com.xtreme.rest.service.test.mock.TestOperationFactory;
import com.xtreme.rest.service.test.mock.TestRestService;
import com.xtreme.rest.service.test.utils.AssertionLatch;

public class RestServiceTest2 extends ServiceTestCase<TestRestService> {

	public RestServiceTest2() {
		super(TestRestService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testRestServiceExtras() {
		assertNotNull(new TestRestService.Extras());
		assertEquals("action", TestRestService.Extras.ACTION);
		assertEquals("operation", TestRestService.Extras.OPERATION);
	}
	
    
    // =============================================
    
    
    public void testRestServiceCancelOperationThrowsException() {
    	final Intent intent = new Intent(getContext(), TestRestService.class);
    	intent.putExtra(TestRestService.Extras.ACTION, Action.CANCEL);
		intent.putExtra(TestRestService.Extras.OPERATION, new TestOperation(null, null));
		try {
			startService(intent);
			fail();
		} catch (final UnsupportedOperationException e) {
			assertNotNull(e);
		}
    }
    
    public void testRestServiceExecutesOperation() {
    	final OperationExecuteCounter latch = new OperationExecuteCounter(1);
    	final Intent intent = new Intent(getContext(), TestRestService.class);
    	intent.putExtra(TestRestService.Extras.ACTION, Action.START);
		intent.putExtra(TestRestService.Extras.OPERATION, new TestOperation(null, null) {
			
			@Override
			public void execute() {
				super.execute();
				latch.execute();
			}
		});
        startService(intent);
        latch.assertComplete();
    }
    
    
    // =============================================
    
    
    public void testRestServiceStartWithOperation() {
    	final ServiceStartCounter latch = new ServiceStartCounter(1);
    	final ContextWrapper context = new ContextWrapper(getContext()) {
    		
    		@Override
    		public ComponentName startService(final Intent service) {
    			latch.startService();
    			assertEquals(RestService.Action.START, service.getSerializableExtra(TestRestService.Extras.ACTION));
    			assertTrue(service.hasExtra(TestRestService.Extras.OPERATION));
    			return null;
    		}
    	};
    	RestService.start(context, TestOperationFactory.newOperationWithoutTasks());
    	latch.assertComplete();
    }
    
    public void testRestServiceCancelWithOperation() {
    	final ServiceStartCounter latch = new ServiceStartCounter(1);
    	final ContextWrapper context = new ContextWrapper(getContext()) {
    		
    		@Override
    		public ComponentName startService(final Intent service) {
    			latch.startService();
    			assertEquals(RestService.Action.CANCEL, service.getSerializableExtra(TestRestService.Extras.ACTION));
    			assertTrue(service.hasExtra(TestRestService.Extras.OPERATION));
    			return null;
    		}
    	};
    	RestService.cancel(context, TestOperationFactory.newOperationWithoutTasks());
    	latch.assertComplete();
    }
	
    
    // =============================================
    
    
    public void testRestServiceDoesntExecuteAlreadyRunningOperation() {
    	startService(new Intent(getContext(), TestRestService.class));
		final TestOperation operation = new TestOperation(null, null);
		getService().addOperation(operation);
		assertFalse(getService().handleStart(operation));
    }
    
    public void testRestServiceShutsDownWhenAllOperationsFinish() {
    	startService(new Intent(getContext(), TestRestService.class));
		final TestOperation operation = new TestOperation(null, null);
		getService().addOperation(operation);
		assertTrue(getService().handleFinish(operation));
    }
    
    public void testRestServiceDoesntShutDownWhenMoreOperationsRunning() {
    	startService(new Intent(getContext(), TestRestService.class));
		final TestOperation operation1 = new TestOperation(null, null);
		final TestOperation operation2 = new TestOperation(null, null);
		getService().addOperation(operation1);
		getService().addOperation(operation2);
		assertFalse(getService().handleFinish(operation1));
    }
    
	
    // =============================================
    
 	private static class ServiceStartCounter {

 		final AssertionLatch mStartLatch;
 		
 		public ServiceStartCounter(final int count) {
 			mStartLatch = new AssertionLatch(count);
 		}
 		
 		public void startService() {
 			mStartLatch.countDown();
 		}
 		
 		public void assertComplete() {
 			mStartLatch.assertComplete();
 		}
 	}
 	
 	private static class OperationExecuteCounter {

 		final AssertionLatch mExecuteLatch;
 		
 		public OperationExecuteCounter(final int count) {
 			mExecuteLatch = new AssertionLatch(count);
 		}
 		
 		public void execute() {
 			mExecuteLatch.countDown();
 		}
 		
 		public void assertComplete() {
 			mExecuteLatch.assertComplete();
 		}
 	}
}
