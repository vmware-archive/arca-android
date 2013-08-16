package com.xtreme.rest.service.test.junit.tests;

import android.test.AndroidTestCase;

import com.xtreme.rest.service.NetworkPrioritizable;
import com.xtreme.rest.service.PrioritizableHandler;
import com.xtreme.rest.service.ProcessingPrioritizable;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;
import com.xtreme.rest.service.TaskObserver;
import com.xtreme.rest.service.test.junit.mock.TestPrioritizableHandler;
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
	
	public void testTaskExecutesPrioritizables() {
		final TestTask task = new TestTask(null);
		task.setPrioritizableHandler(new PrioritizableHandler() {

			@Override
			public void executeNetworkComponent(final PrioritizableRequest request) {
				assertNotNull(request);
			}

			@Override
			public void executeProcessingComponent(final PrioritizableRequest request) {
				assertNotNull(request);
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesWithIdentifier() {
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>("test_identifier");
		
		final TestTask task = new TestTask(identifier);
		task.setPrioritizableHandler(new PrioritizableHandler() {

			@Override
			public void executeNetworkComponent(final PrioritizableRequest request) {
				assertEquals(request.getRequestIdentifier(), identifier);
			}

			@Override
			public void executeProcessingComponent(final PrioritizableRequest request) {
				assertEquals(request.getRequestIdentifier(), identifier);
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesWithoutNetworkError() {
		final TestTask task = new TestTask(null, null);
		task.setPrioritizableHandler(new PrioritizableHandler() {

			@Override
			public void executeNetworkComponent(final PrioritizableRequest request) {
				final NetworkPrioritizable<?> prioritizable = (NetworkPrioritizable<?>) request.getPrioritizable();

				assertNull(prioritizable.getError());
				
				request.run();
				
				assertNull(prioritizable.getError());
			}

			@Override
			public void executeProcessingComponent(final PrioritizableRequest request) {
				// empty
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesWithNetworkResult() {
		final String networkResult = "test_result";
		
		final TestTask task = new TestTask(null, networkResult);
		task.setPrioritizableHandler(new PrioritizableHandler() {

			@Override
			public void executeNetworkComponent(final PrioritizableRequest request) {
				final NetworkPrioritizable<?> prioritizable = (NetworkPrioritizable<?>) request.getPrioritizable();

				assertNull(prioritizable.getData());
				
				request.run();
				
				assertEquals(prioritizable.getData(), networkResult);
			}

			@Override
			public void executeProcessingComponent(final PrioritizableRequest request) {
				// empty
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesWithoutProcessingError() {
		final String networkResult = "test_result";
		
		final TestTask task = new TestTask(null, networkResult);
		task.setPrioritizableHandler(new PrioritizableHandler() {

			@Override
			public void executeNetworkComponent(final PrioritizableRequest request) {
				final NetworkPrioritizable<?> prioritizable = (NetworkPrioritizable<?>) request.getPrioritizable();
				prioritizable.onComplete(null, null);
			}

			@Override
			public void executeProcessingComponent(final PrioritizableRequest request) {
				final ProcessingPrioritizable<?> prioritizable = (ProcessingPrioritizable<?>) request.getPrioritizable();

				assertNull(prioritizable.getError());
				
				request.run();
				
				assertNull(prioritizable.getError());
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesWithProcessingDataFromNetwork() {
		final String networkResult = "test_result";
		
		final TestTask task = new TestTask(null, null);
		task.setPrioritizableHandler(new PrioritizableHandler() {

			@Override
			public void executeNetworkComponent(final PrioritizableRequest request) {
				final NetworkPrioritizable<?> prioritizable = (NetworkPrioritizable<?>) request.getPrioritizable();
				prioritizable.onComplete(networkResult, null);
			}

			@Override
			public void executeProcessingComponent(final PrioritizableRequest request) {
				final ProcessingPrioritizable<?> prioritizable = (ProcessingPrioritizable<?>) request.getPrioritizable();

				assertEquals(prioritizable.getData(), networkResult);
			}
			
		});
		task.execute();
	}
	
	public void testTaskCompletesExecution() {
		final String networkResult = "test_result";
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>("test_identifier");
		
		final TestTask task = new TestTask(identifier, networkResult);
		task.setPrioritizableHandler(new TestPrioritizableHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				// pass
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				// pass
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError error) {
				fail();
			}
		});
		task.execute();
	}
}
