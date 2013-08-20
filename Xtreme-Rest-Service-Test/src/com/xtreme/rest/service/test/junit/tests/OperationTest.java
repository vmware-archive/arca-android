package com.xtreme.rest.service.test.junit.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.AndroidTestCase;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.OperationObserver;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.ServiceException;
import com.xtreme.rest.service.test.junit.mock.TestOperation;
import com.xtreme.rest.service.test.junit.mock.TestOperationFactory;
import com.xtreme.rest.service.test.junit.mock.TestRequestHandler;

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
	
	public void testOperationFinishesWithoutTasks() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithoutTasks();
		operation.setRequestHandler(new TestRequestHandler());
		operation.setOperationObserver(new OperationObserver() {

			@Override
			public void onOperationComplete(final Operation o) {
				latch.onOperationComplete();
				
				assertNotNull(o);
			}
			
		});
		operation.execute();
		latch.assertComplete();
	}
	
	public void testOperationSucceedsWithoutTasks() {
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
	
	public void testOperationSucceedsWithPassingTask() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithPassingTask();
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
	
	public void testOperationFailsWithNetworkError() {
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
	
	public void testOperationFailsWithCustomNetworkError() {
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
	
	public void testOperationSucceedsWithTaskPrerequisites() {
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
	
	public void testOperationSucceedsWithTaskDependants() {
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
	
	// =============================================
	
	private static class AssertionLatch extends CountDownLatch {

		public AssertionLatch(final int count) {
			super(count);
		}
		
		@Override
		public void countDown() {
			final long count = getCount();
			if (count == 0) {
				fail("This latch has already finished.");
			} else {
				super.countDown();
			}
		}
		
		public void assertComplete() {
			try {
				assertTrue(await(0, TimeUnit.SECONDS));
			} catch (final InterruptedException e) {
				fail();
			}
		}
	}
	
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
}
