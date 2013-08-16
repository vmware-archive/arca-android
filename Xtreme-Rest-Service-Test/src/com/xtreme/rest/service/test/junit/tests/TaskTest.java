package com.xtreme.rest.service.test.junit.tests;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.AndroidTestCase;

import com.xtreme.rest.service.NetworkRequest;
import com.xtreme.rest.service.ProcessingRequest;
import com.xtreme.rest.service.RequestHandler;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;
import com.xtreme.rest.service.TaskObserver;
import com.xtreme.rest.service.test.junit.mock.TestRequestHandler;
import com.xtreme.rest.service.test.junit.mock.TestTask;
import com.xtreme.threading.RequestIdentifier;

public class TaskTest extends AndroidTestCase {

	private static final String RESULT = "test_result";
	private static final String IDENTIFIER = "test_identifier";


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
	
	public void testTaskExecutesNetworkPrioritizable() {
		final TestTask task = new TestTask(null);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				assertNotNull(request);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesWithNetworkIdentifier() {
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>(IDENTIFIER);
		final TestTask task = new TestTask(identifier);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				assertEquals(request.getRequestIdentifier(), identifier);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesWithoutNetworkError() {
		final TestTask task = new TestTask(null);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {

				assertNull(request.getError());
				
				request.run();
				
				assertNull(request.getError());
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesWithNetworkResult() {
		final String networkResult = RESULT;
		final TestTask task = new TestTask(null, networkResult);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {

				assertNull(request.getData());
				
				request.run();
				
				assertEquals(request.getData(), networkResult);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesProcessingPrioritizable() {
		final AssertionLatch latch = new AssertionLatch(1);
		final TestTask task = new TestTask(null);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				assertNotNull(request);
				latch.countDown();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesWithProcessingIdentifier() {
		final AssertionLatch latch = new AssertionLatch(1);
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>(IDENTIFIER);
		final TestTask task = new TestTask(identifier);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				assertEquals(request.getRequestIdentifier(), identifier);
				latch.countDown();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesWithoutProcessingError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final String networkResult = RESULT;
		final TestTask task = new TestTask(null, networkResult);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {

				assertNull(request.getError());
				
				request.run();
				
				assertNull(request.getError());
				
				latch.countDown();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesWithProcessingDataFromNetwork() {
		final AssertionLatch latch = new AssertionLatch(1);
		final String networkResult = RESULT;
		final TestTask task = new TestTask(null, null);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				request.notifyComplete(networkResult, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {

				assertEquals(request.getData(), networkResult);
				latch.countDown();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskCompletesExecution() {
		final AssertionLatch latch = new AssertionLatch(2);
		final String networkResult = RESULT;
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>(IDENTIFIER);
		final TestTask task = new TestTask(identifier, networkResult);
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.countDown();
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.countDown();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError error) {
				fail();
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	
	// =============================================
	
	
	private static class AssertionLatch extends CountDownLatch {

		public AssertionLatch(int count) {
			super(count);
		}
		
		public void assertComplete() {
			try {
				assertTrue(await(0, TimeUnit.SECONDS));
			} catch (final InterruptedException e) {
				fail();
			}
		}
	}
}
