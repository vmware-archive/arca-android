package com.xtreme.rest.service.test.junit.tests;


import java.util.List;

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
import com.xtreme.rest.service.test.junit.utils.AssertionLatch;
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
		final RequestCounter latch = new RequestCounter(1, 0);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				assertNotNull(request);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithIdentifier() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>(IDENTIFIER);
		final TestTask task = TestTaskFactory.newTaskWithIdentifier(identifier);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				assertEquals(identifier, request.getRequestIdentifier());
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithoutError() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				assertNull(request.getError());
				
				request.run();
				
				assertNull(request.getError());
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithResult() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final String networkResult = RESULT;
		final TestTask task = TestTaskFactory.newTaskWithNetworkResult(networkResult);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				assertNull(request.getData());
				
				request.run();
				
				assertEquals(networkResult, request.getData());
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithError() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				assertNull(request.getError());
				
				request.run();
				
				assertNotNull(request.getError().getMessage());
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesNetworkRequestWithCustomError() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				assertNull(request.getError());
				
				request.run();
				
				assertEquals(error, request.getError());
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				fail();
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesProcessingRequest() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				assertNotNull(request);
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesProcessingRequestWithIdentifier() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final RequestIdentifier<String> identifier = new RequestIdentifier<String>(IDENTIFIER);
		final TestTask task = TestTaskFactory.newTaskWithIdentifier(identifier);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				assertEquals(identifier, request.getRequestIdentifier());
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesProcessingRequestWithoutError() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				assertNull(request.getError());
				
				request.run();
				
				assertNull(request.getError());
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesProcessingRequestWithDataFromNetwork() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final String networkResult = RESULT;
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				request.notifyComplete(networkResult, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				assertEquals(networkResult, request.getData());
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesProcessingRequestWithError() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				assertNull(request.getError());
				
				request.run();
				
				assertNotNull(request.getError().getMessage());
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesProcessingRequestWithCustomError() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				assertNull(request.getError());
				
				request.run();
				
				assertEquals(error, request.getError());
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	
	// =============================================
	
	
	public void testTaskCompleted() {
		final ObserverCounter latch = new ObserverCounter(1, 1, 0);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertNotNull(t);
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				assertNotNull(t);
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				fail();
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskFailedNetworkRequestWithError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				fail();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertNotNull(t);
				assertNotNull(e.getMessage());
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskFailedNetworkRequestWithCustomError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				fail();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertNotNull(t);
				assertEquals(error, e);
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskFailedProcessingRequestWithError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				fail();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertNotNull(t);
				assertNotNull(e.getMessage());
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskFailedProcessingRequestWithCustomError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task.setRequestHandler(new TestRequestHandler());
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
			}

			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				fail();
			}

			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertNotNull(t);
				assertEquals(error, e);
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	
	// =============================================
	
	
	public void testTaskPrerequisitesSuccess() {
		final ObserverCounter latch = new ObserverCounter(2, 2, 0);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisites();

		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				fail();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				assertEquals(expectedOrder.remove(0), t);
			}
		};

		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskPrerequisitesFirstTaskFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 2);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesFirstTaskFailsWithNetworkException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertEquals(expectedOrder.remove(0), t);
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
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
		final ObserverCounter latch = new ObserverCounter(2, 1, 1);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesSecondTaskFailsWithNetworkException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertEquals(expectedOrder.remove(0), t);
				assertEquals(0, expectedOrder.size());
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				assertEquals(expectedOrder.remove(0), t);
				assertEquals(1, expectedOrder.size());
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskPrerequisitesFirstTaskFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 2);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesFirstTaskFailsWithProcessingException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertEquals(expectedOrder.remove(0), t);
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
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
		final ObserverCounter latch = new ObserverCounter(2, 1, 1);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesSecondTaskFailsWithProcessingException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertEquals(expectedOrder.remove(0), t);
				assertEquals(0, expectedOrder.size());
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				assertEquals(expectedOrder.remove(0), t);
				assertEquals(1, expectedOrder.size());
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDependantsSuccess() {
		final ObserverCounter latch = new ObserverCounter(2, 2, 0);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependants();

		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				fail();
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				assertEquals(expectedOrder.remove(0), t);
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);

		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDependantsFirstTaskFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 2);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependantsFirstTaskFailsWithNetworkException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertEquals(expectedOrder.remove(0), t);
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
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
		final ObserverCounter latch = new ObserverCounter(2, 1, 1);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependantsSecondTaskFailsWithNetworkException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertEquals(expectedOrder.remove(0), t);
				assertEquals(0, expectedOrder.size());
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				assertEquals(expectedOrder.remove(0), t);
				assertEquals(1, expectedOrder.size());
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDependantsFirstTaskFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 2);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependantsFirstTaskFailsWithProcessingException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertEquals(expectedOrder.remove(0), t);
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
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
		final ObserverCounter latch = new ObserverCounter(2, 1, 1);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependantsSecondTaskFailsWithProcessingException(exception);
		
		final TaskObserver observer = new TaskObserver() {
			
			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertEquals(expectedOrder.get(0), t);
			}
			
			@Override
			public void onTaskFailure(final Task<?> t, final ServiceError e) {
				latch.onTaskFailure();
				
				assertEquals(expectedOrder.remove(0), t);
				assertEquals(0, expectedOrder.size());
			}
			
			@Override
			public void onTaskComplete(final Task<?> t) {
				latch.onTaskComplete();
				
				assertEquals(expectedOrder.remove(0), t);
				assertEquals(1, expectedOrder.size());
			}
		};
		
		for (final Task<?> task : expectedOrder) 
			task.setTaskObserver(observer);
		
		expectedOrder.get(1).execute();
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	// =============================================
	
	
	private static class ObserverCounter {

		final AssertionLatch mStartLatch;
		final AssertionLatch mCompleteLatch;
		final AssertionLatch mFailureLatch;
		
		public ObserverCounter(final int startCount, final int completeCount, final int failureCount) {
			mStartLatch = new AssertionLatch(startCount);
			mCompleteLatch = new AssertionLatch(completeCount);
			mFailureLatch = new AssertionLatch(failureCount);
		}
		
		public void onTaskStarted() {
			mStartLatch.countDown();
		}
		
		public void onTaskComplete() {
			mCompleteLatch.countDown();
		}

		public void onTaskFailure() {
			mFailureLatch.countDown();
		}
		
		public void assertComplete() {
			mStartLatch.assertComplete();
			mCompleteLatch.assertComplete();
			mFailureLatch.assertComplete();
		}
	}
	
	private static class RequestCounter {
		
		final AssertionLatch mNetworkLatch;
		final AssertionLatch mProcessingLatch;
		
		public RequestCounter(final int networkCount, final int processingCount) {
			mNetworkLatch = new AssertionLatch(networkCount);
			mProcessingLatch = new AssertionLatch(processingCount);
		}
		
		public void executeNetworkRequest() {
			mNetworkLatch.countDown();
		}
		
		public void executeProcessingRequest() {
			mProcessingLatch.countDown();
		}
		
		public void assertComplete() {
			mNetworkLatch.assertComplete();
			mProcessingLatch.assertComplete();
		}
	}
}
