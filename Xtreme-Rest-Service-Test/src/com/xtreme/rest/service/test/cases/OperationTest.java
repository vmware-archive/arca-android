package com.xtreme.rest.service.test.cases;

import android.net.Uri;
import android.os.Parcel;
import android.test.AndroidTestCase;

import com.xtreme.rest.service.NetworkingRequest;
import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.OperationObserver;
import com.xtreme.rest.service.ProcessingRequest;
import com.xtreme.rest.service.RequestExecutor;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.ServiceException;
import com.xtreme.rest.service.test.mock.TestOperation;
import com.xtreme.rest.service.test.mock.TestOperationFactory;
import com.xtreme.rest.service.test.mock.TestThreadedRequestExecutor;
import com.xtreme.rest.service.test.mock.TestTask;
import com.xtreme.rest.service.test.mock.TestTaskFactory;
import com.xtreme.rest.service.test.utils.AssertionLatch;

public class OperationTest extends AndroidTestCase {

	private static final String ERROR = "test_error";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// =============================================
	
	public void testOperationDoesNotExecuteNullTask() {
		final RequestCounter latch = new RequestCounter(0, 0);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
				fail();
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				fail();
			}
			
		});
		operation.executeTask(null);
		latch.assertComplete();
	}
	
	public void testOperationExecutesTaskNetworkingRequest() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new RequestExecutor() {

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
		operation.executeTask(TestTaskFactory.newTask());
		latch.assertComplete();
	}
	
	public void testOperationExecutesTaskProcessingRequest() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final TestOperation operation = TestOperationFactory.newOperationWithTask();
		operation.setRequestExecutor(new RequestExecutor() {

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
		operation.executeTask(TestTaskFactory.newTask());
		latch.assertComplete();
	}

	
	// =============================================

	public void testOperationExecutedNullTaskSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNull(o.getError());
			}
			
		});
		operation.executeTask(null);
		latch.assertComplete();
	}
	
	public void testOperationExecutedTaskSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNull(o.getError());
			}
			
		});
		operation.executeTask(TestTaskFactory.newTask());
		latch.assertComplete();
	}
	
	public void testOperationExecutedTaskFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNotNull(o.getError());
			}
			
		});
		operation.executeTask(task);
		latch.assertComplete();
	}
	
	public void testOperationExecutedTaskFailsWithCustomNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(error, o.getError());
			}
			
		});
		operation.executeTask(task);
		latch.assertComplete();
	}
	
	public void testOperationExecutedTaskFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNotNull(o.getError());
			}
			
		});
		operation.executeTask(task);
		latch.assertComplete();
	}
	
	public void testOperationExecutedTaskFailsWithCustomProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestTask task = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(error, o.getError());
			}
			
		});
		operation.executeTask(task);
		latch.assertComplete();
	}
	
	
	// =============================================
	
	
	public void testOperationWithoutTasksDoesNotExecuteRequest() {
		final RequestCounter latch = new RequestCounter(0, 0);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new RequestExecutor() {

			@Override
			public void executeNetworkingRequest(final NetworkingRequest<?> request) {
				latch.executeNetworkingRequest();
				
				fail();
			}

			@Override
			public void executeProcessingRequest(final ProcessingRequest<?> request) {
				latch.executeProcessingRequest();
				
				fail();
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskExecutesNetworkingRequest() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final TestOperation operation = TestOperationFactory.newOperationWithTask();
		operation.setRequestExecutor(new RequestExecutor() {

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
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskExecutesProcessingRequest() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final TestOperation operation = TestOperationFactory.newOperationWithTask();
		operation.setRequestExecutor(new RequestExecutor() {

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
		operation.execute();
		latch.assertComplete();
	}
	
	// =============================================
	
	
	public void testOperationWithoutTasksSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithTask();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskThatThrowsNetworkingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNotNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskFailsWithCustomNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskThatThrowsNetworkingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(error, o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskThatThrowsProcessingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNotNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskFailsWithCustomProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskThatThrowsProcessingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(error, o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	// =============================================
	
	public void testOperationWithTaskPrerequisitesSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskPrerequisites();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskPrerequisitesFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskPrerequisitesThatThrowsNetworkingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNotNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskPrerequisitesFailsWithCustomNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskPrerequisitesThatThrowsNetworkingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(error, o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskPrerequisitesFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskPrerequisitesThatThrowsProcessingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNotNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskPrerequisitesFailsWithCustomProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskPrerequisitesThatThrowsProcessingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(error, o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskDependantsSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependants();
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskDependantsFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependantsThatThrowsNetworkingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNotNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskDependantsFailsWithCustomNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependantsThatThrowsNetworkingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(error, o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskDependantsFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependantsThatThrowsProcessingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNotNull(o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskDependantsFailsWithCustomProcssingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependantsThatThrowsProcessingException(exception);
		operation.setRequestExecutor(new TestThreadedRequestExecutor());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(error, o.getError());
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	
	// =============================================
	
	
	public void testOperationParcelableDescribeContents() {
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		assertEquals(0, operation.describeContents());
	}
	
	public void testOperationParcelableCreatorArray() {
		final TestOperation[] operation = TestOperation.CREATOR.newArray(1);
		assertEquals(1, operation.length);
	}
	
	public void testOperationParcelableCreator() {
		final Uri uri = Uri.parse("http://empty");
		final TestOperation operation = TestOperationFactory.newOperationWithUri(uri);
		
		final Parcel parcel = Parcel.obtain();
		operation.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		
		final TestOperation parceled = TestOperation.CREATOR.createFromParcel(parcel);
		assertEquals(uri, parceled.getUri());
		
		parcel.recycle();
	}
	
	
	// =============================================

	
	private static class ObserverCounter {

		final AssertionLatch mCompleteLatch;
		
		public ObserverCounter(final int completeCount) {
			mCompleteLatch = new AssertionLatch(completeCount);
		}
		
		public void onOperationComplete() {
			mCompleteLatch.countDown();
		}
		
		public void assertComplete() {
			mCompleteLatch.assertComplete();
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
