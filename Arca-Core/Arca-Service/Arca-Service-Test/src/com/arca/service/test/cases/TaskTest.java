package com.arca.service.test.cases;

import java.util.List;

import android.test.AndroidTestCase;

import com.arca.service.NetworkingRequest;
import com.arca.service.ProcessingRequest;
import com.arca.service.RequestExecutor;
import com.arca.service.RequestExecutor.SerialRequestExecutor;
import com.arca.service.ServiceError;
import com.arca.service.ServiceException;
import com.arca.service.Task;
import com.arca.service.TaskObserver;
import com.arca.service.test.mock.TestTask;
import com.arca.service.test.mock.TestTaskFactory;
import com.arca.service.test.utils.AssertionLatch;
import com.arca.threading.Identifier;

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
	
	public void testTaskMessages() {
		assertEquals("Cannot execute request. No request executor found.", TestTask.Messages.NO_EXECUTOR);
	}
	
	// =============================================
	
	public void testTaskExecutesNetworkingRequest() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
	
	public void testTaskExecutesNetworkingRequestWithIdentifier() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final Identifier<String> identifier = new Identifier<String>(IDENTIFIER);
		final TestTask task = TestTaskFactory.newTaskWithIdentifier(identifier);
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
				assertEquals(identifier, request.getIdentifier());
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
	
	public void testTaskExecutesNetworkingRequestWithoutError() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
	
	public void testTaskExecutesNetworkingRequestWithResult() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final String networkResult = RESULT;
		final TestTask task = TestTaskFactory.newTaskWithNetworkingResult(networkResult);
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
	
	public void testTaskExecutesNetworkingRequestWithError() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
	
	public void testTaskExecutesNetworkingRequestWithCustomError() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
		final Identifier<String> identifier = new Identifier<String>(IDENTIFIER);
		final TestTask task = TestTaskFactory.newTaskWithIdentifier(identifier);
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
				request.notifyComplete(null, null);
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				assertEquals(identifier, request.getIdentifier());
			}
			
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskExecutesProcessingRequestWithoutError() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
		task.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
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
	
	
	public void testTaskFailsWithoutExecutorForNetworkingRequest() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 1);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestExecutor(null);
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				assertNotNull(t);
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
				assertEquals(TestTask.Messages.NO_EXECUTOR, e.getMessage());
			}
		});
		task.execute();
		latch.assertComplete();
	}
	
	public void testTaskFailsWithoutExecutorForProcessingRequest() {
		final ObserverCounter latch = new ObserverCounter(0, 0, 1);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestExecutor(null);
		task.setTaskObserver(new TaskObserver() {

			@Override
			public void onTaskStarted(final Task<?> t) {
				latch.onTaskStarted();
				
				fail();
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
				assertEquals(TestTask.Messages.NO_EXECUTOR, e.getMessage());
			}
		});
		task.onNetworkingComplete(null);
		latch.assertComplete();
	}
	
	
	public void testTaskCompleted() {
		final ObserverCounter latch = new ObserverCounter(1, 1, 0);
		final TestTask task = TestTaskFactory.newTask();
		task.setRequestExecutor(new SerialRequestExecutor());
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
	
	public void testTaskFailedNetworkingRequestWithError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		task.setRequestExecutor(new SerialRequestExecutor());
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
	
	public void testTaskFailedNetworkingRequestWithCustomError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		task.setRequestExecutor(new SerialRequestExecutor());
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
		task.setRequestExecutor(new SerialRequestExecutor());
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
		task.setRequestExecutor(new SerialRequestExecutor());
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
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesFirstTaskFailsWithNetworkingException(exception);
		
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
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithPrerequisitesSecondTaskFailsWithNetworkingException(exception);
		
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
	
	public void testTaskDependenciesSuccess() {
		final ObserverCounter latch = new ObserverCounter(2, 2, 0);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependencies();

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
	
	public void testTaskDependenciesFirstTaskFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 2);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependenciesFirstTaskFailsWithNetworkingException(exception);
		
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
	
	public void testTaskDependenciesSecondTaskFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(2, 1, 1);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependenciesSecondTaskFailsWithNetworkingException(exception);
		
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
	
	public void testTaskDependenciesFirstTaskFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1, 0, 2);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependenciesFirstTaskFailsWithProcessingException(exception);
		
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
	
	public void testTaskDependenciesSecondTaskFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(2, 1, 1);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDependenciesSecondTaskFailsWithProcessingException(exception);
		
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
	
	public void testTaskDynamicDependenciesSuccess() {
		final ObserverCounter latch = new ObserverCounter(2, 2, 0);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDynamicDependencies();

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

		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDynamicDependenciesSecondTaskFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(2, 1, 1);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDynamicDependenciesSecondTaskFailsWithNetworkingException(exception);
		
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
		
		expectedOrder.get(0).execute();
		
		latch.assertComplete();
	}
	
	public void testTaskDynamicDependenciesSecondTaskFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(2, 1, 1);
		final Exception exception = new Exception(ERROR);
		final List<Task<?>> expectedOrder = TestTaskFactory.newTaskListWithDynamicDependenciesSecondTaskFailsWithProcessingException(exception);
		
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
		
		public void executeNetworkingRequest() {
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
