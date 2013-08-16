package com.xtreme.rest.service.test.junit.tests;

import android.test.AndroidTestCase;

import com.xtreme.rest.service.NetworkPrioritizable;
import com.xtreme.rest.service.PrioritizableHandler;
import com.xtreme.rest.service.Priority;
import com.xtreme.rest.service.ProcessingPrioritizable;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.test.junit.mock.TestTask;
import com.xtreme.threading.PrioritizableRequest;
import com.xtreme.threading.RequestIdentifier;

public class TaskTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testInitialization() {
		// success
	}
	
	public void testSimple() {
		final String networkResult = "test_result";
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>("test_identifier");
		
		final TestTask task = new TestTask(identifier, networkResult);
		task.setPriority(Priority.HIGH);
		task.setPrioritizableHandler(new PrioritizableHandler() {

			@Override
			public void executeNetworkComponent(final PrioritizableRequest request) {
				assertNotNull(request);
				assertNotNull(request.getRequestIdentifier());
				assertEquals(request.getRequestIdentifier(), identifier);
				
				request.run();
				
				final NetworkPrioritizable<?> result = (NetworkPrioritizable<?>) request.getPrioritizable();
				
				assertNotNull(result);
				
				final ServiceError error = result.getError();
				final Object object = result.getData();
				
				assertNull(error);
				assertNotNull(object);
				assertEquals(object, networkResult);
			}

			@Override
			public void executeProcessingComponent(final PrioritizableRequest request) {
				assertNotNull(request);
				assertNotNull(request.getRequestIdentifier());
				assertEquals(request.getRequestIdentifier(), identifier);
				
				request.run();
				
				final ProcessingPrioritizable<?> result = (ProcessingPrioritizable<?>) request.getPrioritizable();
				
				assertNotNull(result);
				
				final ServiceError error = result.getError();
				
				assertNull(error);
			}
			
		});
		task.execute();
	}
}
