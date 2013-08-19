package com.xtreme.rest.service.test.junit.tests;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.AndroidTestCase;

import com.xtreme.rest.service.NetworkRequest;
import com.xtreme.rest.service.ProcessingRequest;
import com.xtreme.rest.service.RequestHandler;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.ServiceException;
import com.xtreme.rest.service.Task;
import com.xtreme.rest.service.TaskObserver;
import com.xtreme.rest.service.test.junit.mock.TestRequestHandler;
import com.xtreme.rest.service.test.junit.mock.TestTask;
import com.xtreme.rest.service.test.junit.mock.TestTaskFactory;
import com.xtreme.threading.RequestIdentifier;

public class TaskTest extends AndroidTestCase {

	private static final String ERROR = "test_error";
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
	
	
	// =============================================
	
	
	public void testTaskExecutesNetworkRequest() {
		final TestTask task = TestTaskFactory.newTask();
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
	
	public void testTaskExecutesNetworkRequestWithIdentifier() {
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>(IDENTIFIER);
		final TestTask task = TestTaskFactory.newTaskWithIdentifier(identifier);
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
	
	public void testTaskExecutesNetworkRequestWithoutError() {
		final TestTask task = TestTaskFactory.newTask();
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
	
	public void testTaskExecutesNetworkRequestWithResult() {
		final String networkResult = RESULT;
		final TestTask task = TestTaskFactory.newTaskWithNetworkResult(networkResult);
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
	
	public void testTaskExecutesNetworkRequestWithError() {
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {

				assertNull(request.getError());
				
				request.run();
				
				assertNotNull(request.getError().getMessage());
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesNetworkRequestWithCustomError() {
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				
				assertNull(request.getError());
				
				request.run();
				
				assertEquals(request.getError(), error);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
	}
	
	public void testTaskExecutesProcessingRequest() {
		final AssertionLatch latch = new AssertionLatch(1);
		final TestTask task = TestTaskFactory.newTask();
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
	
	public void testTaskExecutesProcessingRequestWithIdentifier() {
		final AssertionLatch latch = new AssertionLatch(1);
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>(IDENTIFIER);
		final TestTask task = TestTaskFactory.newTaskWithIdentifier(identifier);
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
	
	public void testTaskExecutesProcessingRequestWithoutError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final TestTask task = TestTaskFactory.newTask();
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
	
	public void testTaskExecutesProcessingRequestWithDataFromNetwork() {
		final AssertionLatch latch = new AssertionLatch(1);
		final String networkResult = RESULT;
		final TestTask task = TestTaskFactory.newTask();
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
	
	public void testTaskExecutesProcessingRequestWithError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				
				assertNull(request.getError());
				
				request.run();
				
				assertNotNull(request.getError().getMessage());
				
				latch.countDown();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesProcessingRequestWithCustomError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				
				assertNull(request.getError());
				
				request.run();
				
				assertEquals(request.getError(), error);
				
				latch.countDown();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	
	// =============================================
	
	
	public void testTaskStarted() {
		final AssertionLatch latch = new AssertionLatch(1);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertNotNull(t);
				
				latch.countDown();
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				// 
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				fail();
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskCompleted() {
		final AssertionLatch latch = new AssertionLatch(1);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				//
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				
				assertNotNull(t);
				
				latch.countDown();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				fail();
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskFailedNetworkPortionWithError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				//
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				fail();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertNotNull(t);
				assertNotNull(e.getMessage());
				
				latch.countDown();
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskFailedNetworkPortionWithCustomError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				// 
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				fail();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertNotNull(t);
				assertEquals(e, error);
				
				latch.countDown();
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskFailedProcessingPortionWithError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				//
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				fail();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertNotNull(t);
				assertNotNull(e.getMessage());
				
				latch.countDown();
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskFailedProcessingPortionWithCustomError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				// 
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				fail();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertNotNull(t);
				assertEquals(e, error);
				
				latch.countDown();
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	// =============================================
	
	
	public void testTaskLinearPrerequisites() {
		final AssertionLatch latch = new AssertionLatch(2);
		final Queue<Task<?>> expectedOrder = new LinkedList<Task<?>>();
		final TestRequestHandler handler = new TestRequestHandler();
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.element());
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				fail();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				
				assertEquals(t, expectedOrder.remove());
			}
		};
		
		final TestTask task1 = TestTaskFactory.newTask();
		task1.setRequestHandler(handler);
		task1.setTaskObserver(observer);
		
		final TestTask task2 = TestTaskFactory.newTask();
		task2.setRequestHandler(handler);
		task2.setTaskObserver(observer);
		task2.addPrerequisite(task1);

		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		task2.execute();
		task1.execute();
		
		latch.assertComplete();
	}
	
	public void testTaskLinearDependants() {
		final AssertionLatch latch = new AssertionLatch(2);
		final Queue<Task<?>> expectedOrder = new LinkedList<Task<?>>();
		final TestRequestHandler handler = new TestRequestHandler();
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.element());
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				fail();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				
				assertEquals(t, expectedOrder.remove());
			}
		};
		
		final TestTask task2 = TestTaskFactory.newTask();
		task2.setRequestHandler(handler);
		task2.setTaskObserver(observer);
		
		final TestTask task1 = TestTaskFactory.newTask();
		task1.setRequestHandler(handler);
		task1.setTaskObserver(observer);
		task1.addDependant(task2);

		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		task2.execute();
		task1.execute();
		
		latch.assertComplete();
	}
	
	
	// =============================================
	
	
	private static class AssertionLatch extends CountDownLatch {

		public AssertionLatch(final int count) {
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
