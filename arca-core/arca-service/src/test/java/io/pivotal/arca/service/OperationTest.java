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
package io.pivotal.arca.service;

import android.net.Uri;
import android.os.Parcel;
import android.test.AndroidTestCase;

import junit.framework.Assert;

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

	public void testOperationWithTaskDependenciesSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependencies();
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

	public void testOperationWithTaskDependenciesFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependenciesThatThrowsNetworkingException(exception);
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

	public void testOperationWithTaskDependenciesFailsWithCustomNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependenciesThatThrowsNetworkingException(exception);
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

	public void testOperationWithTaskDependenciesFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependenciesThatThrowsProcessingException(exception);
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

	public void testOperationWithTaskDependenciesFailsWithCustomProcssingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithTaskDependenciesThatThrowsProcessingException(exception);
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

	public void testOperationWithDynamicTaskDependenciesSucceeds() {
		final ObserverCounter latch = new ObserverCounter(1);
		final TestOperation operation = TestOperationFactory.newOperationWithDynamicTaskDependency();
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

	public void testOperationWithDynamicTaskDependenciesFailsWithNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithDynamicTaskDependenciesThatThrowsNetworkingException(exception);
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

	public void testOperationWithDynamicTaskDependenciesFailsWithCustomNetworkError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithDynamicTaskDependenciesThatThrowsNetworkingException(exception);
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

	public void testOperationWithDynamicTaskDependenciesFailsWithProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final Exception exception = new Exception(ERROR);
		final TestOperation operation = TestOperationFactory.newOperationWithDynamicTaskDependenciesThatThrowsProcessingException(exception);
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

	public void testOperationWithDynamicTaskDependenciesFailsWithCustomProcessingError() {
		final ObserverCounter latch = new ObserverCounter(1);
		final ServiceError error = new ServiceError(ERROR);
		final ServiceException exception = new ServiceException(error);
		final TestOperation operation = TestOperationFactory.newOperationWithDynamicTaskDependenciesThatThrowsProcessingException(exception);
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
		Assert.assertEquals(0, operation.describeContents());
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
		Assert.assertEquals(uri, parceled.getUri());

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
