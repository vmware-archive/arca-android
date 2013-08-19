package com.xtreme.rest.service.test.junit.tests;


import java.util.List;
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
		final AssertionLatch latch = new AssertionLatch(1);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				
				assertNotNull(request);
				
				latch.countDown();
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithIdentifier() {
		final AssertionLatch latch = new AssertionLatch(1);
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>(IDENTIFIER);
		final TestTask task = TestTaskFactory.newTaskWithIdentifier(identifier);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				
				assertEquals(request.getRequestIdentifier(), identifier);
				
				latch.countDown();
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithoutError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {

				assertNull(request.getError());
				
				request.run();
				
				assertNull(request.getError());
				
				latch.countDown();
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithResult() {
		final AssertionLatch latch = new AssertionLatch(1);
		final String networkResult = RESULT;
		final TestTask task = TestTaskFactory.newTaskWithNetworkResult(networkResult);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {

				assertNull(request.getData());
				
				request.run();
				
				assertEquals(request.getData(), networkResult);
				
				latch.countDown();
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {

				assertNull(request.getError());
				
				request.run();
				
				assertNotNull(request.getError().getMessage());
				
				latch.countDown();
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithCustomError() {
		final AssertionLatch latch = new AssertionLatch(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				
				assertNull(request.getError());
				
				request.run();
				
				assertEquals(request.getError(), error);
				
				latch.countDown();
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
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
	
	public void testTaskFailedNetworkRequestWithError() {
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
	
	public void testTaskFailedNetworkRequestWithCustomError() {
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
	
	public void testTaskFailedProcessingRequestWithError() {
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
	
	public void testTaskFailedProcessingRequestWithCustomError() {
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
	
	
	public void testTaskPrerequisitesSuccess() {
		final AssertionLatch latch = new AssertionLatch(4);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisites();

		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				fail();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				
				assertEquals(t, expectedOrder.remove(0));
				
				latch.countDown();
			}
		};

		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskPrerequisitesFirstTaskFailsWithNetworkError() {
		final AssertionLatch latch = new AssertionLatch(3);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesFirstTaskFailsWithNetworkException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertEquals(t, expectedOrder.remove(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				fail();
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskPrerequisitesSecondTaskFailsWithNetworkError() {
		final AssertionLatch latch = new AssertionLatch(4);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesSecondTaskFailsWithNetworkException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertEquals(t, expectedOrder.remove(0));
				assertEquals(expectedOrder.size(), 0);
				
				latch.countDown();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				
				assertEquals(t, expectedOrder.remove(0));
				assertEquals(expectedOrder.size(), 1);
				
				latch.countDown();
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskPrerequisitesFirstTaskFailsWithProcessingError() {
		final AssertionLatch latch = new AssertionLatch(3);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesFirstTaskFailsWithProcessingException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertEquals(t, expectedOrder.remove(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				fail();
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskPrerequisitesSecondTaskFailsWithProcessingError() {
		final AssertionLatch latch = new AssertionLatch(4);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesSecondTaskFailsWithProcessingException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertEquals(t, expectedOrder.remove(0));
				assertEquals(expectedOrder.size(), 0);
				
				latch.countDown();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				
				assertEquals(t, expectedOrder.remove(0));
				assertEquals(expectedOrder.size(), 1);
				
				latch.countDown();
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDependantsSuccess() {
		final AssertionLatch latch = new AssertionLatch(4);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependants();

		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				fail();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				
				assertEquals(t, expectedOrder.remove(0));
				
				latch.countDown();
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);

		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDependantsFirstTaskFailsWithNetworkError() {
		final AssertionLatch latch = new AssertionLatch(3);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependantsFirstTaskFailsWithNetworkException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertEquals(t, expectedOrder.remove(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				fail();
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDependantsSecondTaskFailsWithNetworkError() {
		final AssertionLatch latch = new AssertionLatch(4);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependantsSecondTaskFailsWithNetworkException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertEquals(t, expectedOrder.remove(0));
				assertEquals(expectedOrder.size(), 0);
				
				latch.countDown();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				
				assertEquals(t, expectedOrder.remove(0));
				assertEquals(expectedOrder.size(), 1);
				
				latch.countDown();
				
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDependantsFirstTaskFailsWithProcessingError() {
		final AssertionLatch latch = new AssertionLatch(3);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependantsFirstTaskFailsWithProcessingException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertEquals(t, expectedOrder.remove(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				fail();
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDependantsSecondTaskFailsWithProcessingError() {
		final AssertionLatch latch = new AssertionLatch(4);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependantsSecondTaskFailsWithProcessingException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				
				assertEquals(t, expectedOrder.get(0));
				
				latch.countDown();
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				
				assertEquals(t, expectedOrder.remove(0));
				assertEquals(expectedOrder.size(), 0);
				
				latch.countDown();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				
				assertEquals(t, expectedOrder.remove(0));
				assertEquals(expectedOrder.size(), 1);
				
				latch.countDown();
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
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
