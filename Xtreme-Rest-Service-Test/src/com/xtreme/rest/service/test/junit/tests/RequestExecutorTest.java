package com.xtreme.rest.service.test.junit.tests;


import android.test.AndroidTestCase;

import com.xtreme.rest.service.NetworkRequest;
import com.xtreme.rest.service.test.junit.mock.TestRequestExecutor;
import com.xtreme.rest.service.test.junit.mock.TestNetworkRequest;
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

	public void test() {
		final NetworkRequestCounter latch = new NetworkRequestCounter(1);
		final TestNetworkPrioritizable prioritizable = new TestNetworkPrioritizable();
		final TestNetworkRequest request = new TestNetworkRequest(prioritizable);
		final TestRequestExecutor executor = new TestRequestExecutor() {
			
			@Override
			public void onNetworkRequestComplete(final NetworkRequest<?> r) {
				latch.onNetworkRequestComplete();
				
				assertNotNull(r);
			}
		};
		executor.executeNetworkRequest(request);
		latch.assertComplete();
	}

	
	// =============================================
	
	
	private static class NetworkRequestCounter {

		final AssertionLatch mCompleteLatch;
		
		public NetworkRequestCounter(final int completeCount) {
			mCompleteLatch = new AssertionLatch(completeCount);
		}
		
		public void onNetworkRequestComplete() {
			mCompleteLatch.countDown();
		}
		
		public void assertComplete() {
			mCompleteLatch.assertComplete();
		}
	}
}
