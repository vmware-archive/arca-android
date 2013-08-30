package com.xtreme.rest.service.test.junit.tests;


import android.test.AndroidTestCase;

import com.xtreme.rest.service.NetworkRequest;
import com.xtreme.rest.service.ProcessingRequest;
import com.xtreme.rest.service.test.junit.mock.TestNetworkPrioritizable;
import com.xtreme.rest.service.test.junit.mock.TestNetworkRequest;
import com.xtreme.rest.service.test.junit.mock.TestProcessingPrioritizable;
import com.xtreme.rest.service.test.junit.mock.TestProcessingRequest;
import com.xtreme.rest.service.test.junit.mock.TestRequestExecutor;
import com.xtreme.rest.service.test.junit.utils.AssertionLatch;

public class RequestExecutorTest extends AndroidTestCase {


	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testNetworkRequestCompletes() {
		final RequestHandlerCounter latch = new RequestHandlerCounter(1, 0);
		final TestNetworkPrioritizable prioritizable = new TestNetworkPrioritizable();
		final TestNetworkRequest request = new TestNetworkRequest(prioritizable);
		final TestRequestExecutor executor = new TestRequestExecutor() {
			
			@Override
			public void onNetworkRequestComplete(final NetworkRequest<?> r) {
				latch.onNetworkRequestComplete();
				
				assertEquals(request, r);
			}
		};
		executor.executeNetworkRequest(request);
		latch.assertComplete();
	}
	
	public void testProcessingRequestCompletes() {
		final RequestHandlerCounter latch = new RequestHandlerCounter(0, 1);
		final TestProcessingPrioritizable prioritizable = new TestProcessingPrioritizable();
		final TestProcessingRequest request = new TestProcessingRequest(prioritizable);
		final TestRequestExecutor executor = new TestRequestExecutor() {
			
			@Override
			public void onProcessingRequestComplete(final ProcessingRequest<?> r) {
				latch.onProcessingRequestComplete();
				
				assertEquals(request, r);
			}
		};
		executor.executeProcessingRequest(request);
		latch.assertComplete();
	}

	
	// =============================================
	
	
	private static class RequestHandlerCounter {

		private final AssertionLatch mNetworkLatch;
		private final AssertionLatch mProcessingLatch;
		
		public RequestHandlerCounter(final int networkCount, final int processingCount) {
			mNetworkLatch = new AssertionLatch(networkCount);
			mProcessingLatch = new AssertionLatch(processingCount);
		}
		
		public void onNetworkRequestComplete() {
			mNetworkLatch.countDown();
		}
		
		public void onProcessingRequestComplete() {
			mProcessingLatch.countDown();
		}
		
		public void assertComplete() {
			mNetworkLatch.assertComplete();
			mProcessingLatch.assertComplete();
		}
	}
}
