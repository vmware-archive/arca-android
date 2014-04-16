/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.service.test.cases;

import android.test.AndroidTestCase;

import io.pivotal.arca.service.NetworkingRequest;
import io.pivotal.arca.service.ProcessingRequest;
import io.pivotal.arca.service.test.mock.TestNetworkingPrioritizable;
import io.pivotal.arca.service.test.mock.TestNetworkingRequest;
import io.pivotal.arca.service.test.mock.TestProcessingPrioritizable;
import io.pivotal.arca.service.test.mock.TestProcessingRequest;
import io.pivotal.arca.service.test.mock.TestThreadedRequestExecutor;
import io.pivotal.arca.service.test.utils.AssertionLatch;

import junit.framework.Assert;

import io.pivotal.arca.service.RequestExecutor;

public class RequestExecutorTest extends AndroidTestCase {

	public void testRequestExecutorConfig() {
		Assert.assertEquals(2, RequestExecutor.ThreadedRequestExecutor.Config.NUM_NETWORK_THREADS);
		Assert.assertEquals(1, RequestExecutor.ThreadedRequestExecutor.Config.NUM_PROCESSING_THREADS);
		Assert.assertEquals(15, RequestExecutor.ThreadedRequestExecutor.Config.THREAD_KEEP_ALIVE_TIME);
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
