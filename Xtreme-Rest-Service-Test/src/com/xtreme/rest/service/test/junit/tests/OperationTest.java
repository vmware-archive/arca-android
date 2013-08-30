package com.xtreme.rest.service.test.junit.tests;

import android.test.AndroidTestCase;

import com.xtreme.rest.service.NetworkRequest;
import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.OperationObserver;
import com.xtreme.rest.service.ProcessingRequest;
import com.xtreme.rest.service.RequestHandler;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.ServiceException;
import com.xtreme.rest.service.test.junit.mock.TestOperation;
import com.xtreme.rest.service.test.junit.mock.TestOperationFactory;
import com.xtreme.rest.service.test.junit.mock.TestRequestHandler;
import com.xtreme.rest.service.test.junit.utils.AssertionLatch;

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
	
	public void testOperationWithoutTasksDoesNotExecuteRequest() {
		final RequestCounter latch = new RequestCounter(0, 0);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestHandler(new RequestHandler() {

			@Override
			public void executeNetworkRequest(final NetworkRequest<?> request) {
				latch.executeNetworkRequest();
				
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
	
	public void testOperationWithTaskExecutesNetworkRequest() {
		final RequestCounter latch = new RequestCounter(1, 0);
		final TestOperation operation = TestOperationFactory.newOperationWithTask();
		operation.setRequestHandler(new RequestHandler() {

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
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskExecutesProcessingRequest() {
		final RequestCounter latch = new RequestCounter(1, 1);
		final TestOperation operation = TestOperationFactory.newOperationWithTask();
		operation.setRequestHandler(new RequestHandler() {

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
		operation.execute();
		latch.assertComplete();
	}
	
	// =============================================
	
	
	public void testOperationWithoutTasksSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestHandler(new TestRequestHandler());
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
		operation.setRequestHandler(new TestRequestHandler());
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
		final TestOperation operation = TestOperationFactory.newOperationWithTaskThatThrowsNetworkException(exception);
		operation.setRequestHandler(new TestRequestHandler());
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
		final TestOperation operation = TestOperationFactory.newOperationWithTaskThatThrowsNetworkException(exception);
		operation.setRequestHandler(new TestRequestHandler());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(o.getError(), error);
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskThatThrowsProcessingException(exception);
		operation.setRequestHandler(new TestRequestHandler());
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
		operation.setRequestHandler(new TestRequestHandler());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(o.getError(), error);
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	// =============================================
	
	public void testOperationWithTaskPrerequisitesSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskPrerequisites();
		operation.setRequestHandler(new TestRequestHandler());
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
		final TestOperation operation = TestOperationFactory.newOperationWithTaskPrerequisitesThatThrowsNetworkException(exception);
		operation.setRequestHandler(new TestRequestHandler());
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
		final TestOperation operation = TestOperationFactory.newOperationWithTaskPrerequisitesThatThrowsNetworkException(exception);
		operation.setRequestHandler(new TestRequestHandler());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(o.getError(), error);
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskPrerequisitesFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskPrerequisitesThatThrowsProcessingException(exception);
		operation.setRequestHandler(new TestRequestHandler());
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
		operation.setRequestHandler(new TestRequestHandler());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(o.getError(), error);
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskDependantsSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependants();
		operation.setRequestHandler(new TestRequestHandler());
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
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependantsThatThrowsNetworkException(exception);
		operation.setRequestHandler(new TestRequestHandler());
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
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependantsThatThrowsNetworkException(exception);
		operation.setRequestHandler(new TestRequestHandler());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(o.getError(), error);
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationWithTaskDependantsFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependantsThatThrowsProcessingException(exception);
		operation.setRequestHandler(new TestRequestHandler());
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
		operation.setRequestHandler(new TestRequestHandler());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertEquals(o.getError(), error);
			}
			
		});
		operation.execute();
		latch.assertComplete();
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
