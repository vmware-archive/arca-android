package com.xtreme.rest.service.test.cases;


import android.test.AndroidTestCase;

import com.xtreme.rest.service.NetworkingRequest;
import com.xtreme.rest.service.ProcessingRequest;
import com.xtreme.rest.service.RequestExecutor.ThreadedRequestExecutor;
import com.xtreme.rest.service.test.mock.TestNetworkingPrioritizable;
import com.xtreme.rest.service.test.mock.TestNetworkingRequest;
import com.xtreme.rest.service.test.mock.TestProcessingPrioritizable;
import com.xtreme.rest.service.test.mock.TestProcessingRequest;
import com.xtreme.rest.service.test.mock.TestThreadedRequestExecutor;
import com.xtreme.rest.service.test.utils.AssertionLatch;

public class RequestExecutorTest extends AndroidTestCase {

	
	public void testRequestExecutorConfig() {
		assertEquals(2, ThreadedRequestExecutor.Config.NUM_NETWORK_THREADS);
		assertEquals(1, ThreadedRequestExecutor.Config.NUM_PROCESSING_THREADS);
		assertEquals(15, ThreadedRequestExecutor.Config.THREAD_KEEP_ALIVE_TIME);
	}
	
	
	// =============================================
	
	
	public void testRequestExecutorInitiallyEmpty() {
		final TestThreadedRequestExecutor executor = new TestThreadedRequestExecutor();
		assertEquals(0, executor.getRequestCount());
		assertTrue(executor.isEmpty());
	}
	
	public void testRequestExecutorEmptyAfterExecutingNetworkRequest() {
		final TestNetworkingPrioritizable prioritizable = new TestNetworkingPrioritizable();
		final TestNetworkingRequest request = new TestNetworkingRequest(prioritizable);
		final TestThreadedRequestExecutor executor = new TestThreadedRequestExecutor();
		executor.executeNetworkingRequest(request);
		assertEquals(0, executor.getRequestCount());
		assertTrue(executor.isEmpty());
	}
	
	public void testRequestExecutorEmptyAfterExecutingProcessingRequest() {
		final TestProcessingPrioritizable prioritizable = new TestProcessingPrioritizable();
		final TestProcessingRequest request = new TestProcessingRequest(prioritizable);
		final TestThreadedRequestExecutor executor = new TestThreadedRequestExecutor();
		executor.executeProcessingRequest(request);
		assertEquals(0, executor.getRequestCount());
		assertTrue(executor.isEmpty());
	}
	
	
	// =============================================
	
	
	public void testRequestExecutorNetworkingRequestCompletes() {
		final RequestHandlerCounter latch = new RequestHandlerCounter(1, 0);
		final TestNetworkingPrioritizable prioritizable = new TestNetworkingPrioritizable();
		final TestNetworkingRequest request = new TestNetworkingRequest(prioritizable);
		final TestThreadedRequestExecutor executor = new TestThreadedRequestExecutor() {
			
			@Override
			public void onNetworkingRequestComplete(final NetworkingRequest<?> r) {
				super.onNetworkingRequestComplete(r);
				latch.onNetworkingRequestComplete();
				
				assertEquals(request, r);
			}
		};
		executor.executeNetworkingRequest(request);
		latch.assertComplete();
	}
	
	public void testRequestExecutorProcessingRequestCompletes() {
		final RequestHandlerCounter latch = new RequestHandlerCounter(0, 1);
		final TestProcessingPrioritizable prioritizable = new TestProcessingPrioritizable();
		final TestProcessingRequest request = new TestProcessingRequest(prioritizable);
		final TestThreadedRequestExecutor executor = new TestThreadedRequestExecutor() {
			
			@Override
			public void onProcessingRequestComplete(final ProcessingRequest<?> r) {
				super.onProcessingRequestComplete(r);
				latch.onProcessingRequestComplete();
				
				assertEquals(request, r);
			}
		};
		executor.executeProcessingRequest(request);
		latch.assertComplete();
	}
	
	
	// =============================================
	
	
	public void testRequestExecutorNetworkingRequestCancelled() {
		final RequestHandlerCounter latch = new RequestHandlerCounter(1, 0);
		final TestNetworkingPrioritizable prioritizable = new TestNetworkingPrioritizable();
		final TestNetworkingRequest request = new TestNetworkingRequest(prioritizable);
		final TestThreadedRequestExecutor executor = new TestThreadedRequestExecutor() {
			
			@Override
			public void onNetworkingRequestCancelled(final NetworkingRequest<?> r) {
				super.onNetworkingRequestCancelled(r);
				latch.onNetworkingRequestCancelled();
				
				assertEquals(request, r);
			}
		};
		request.cancel();
		executor.executeNetworkingRequest(request);
		latch.assertComplete();
	}
	
	public void testRequestExecutorProcessingRequestCancelled() {
		final RequestHandlerCounter latch = new RequestHandlerCounter(0, 1);
		final TestProcessingPrioritizable prioritizable = new TestProcessingPrioritizable();
		final TestProcessingRequest request = new TestProcessingRequest(prioritizable);
		final TestThreadedRequestExecutor executor = new TestThreadedRequestExecutor() {
			
			@Override
			public void onProcessingRequestCancelled(final ProcessingRequest<?> r) {
				super.onProcessingRequestCancelled(r);
				latch.onProcessingRequestCancelled();
				
				assertEquals(request, r);
			}
		};
		request.cancel();
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
		
		public void onNetworkingRequestComplete() {
			mNetworkLatch.countDown();
		}
		
		public void onNetworkingRequestCancelled() {
			mNetworkLatch.countDown();
		}
		
		public void onProcessingRequestComplete() {
			mProcessingLatch.countDown();
		}
		
		public void onProcessingRequestCancelled() {
			mProcessingLatch.countDown();
		}
		
		public void assertComplete() {
			mNetworkLatch.assertComplete();
			mProcessingLatch.assertComplete();
		}
	}
}
