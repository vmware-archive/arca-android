/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import android.content.ComponentName;
import android.content.ContextWrapper;
import android.content.Intent;
import android.test.ServiceTestCase;

public class OperationServiceTest2 extends ServiceTestCase<TestOperationService> {

	public OperationServiceTest2() {
		super(TestOperationService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testOperationServiceExtras() {
		assertEquals("action", TestOperationService.Extras.ACTION);
		assertEquals("operation", TestOperationService.Extras.OPERATION);
	}

	// =============================================

	public void testOperationServiceExecutesOperation() {
		final OperationExecuteCounter latch = new OperationExecuteCounter(1);
		final Intent intent = new Intent(getContext(), TestOperationService.class);
		intent.putExtra(TestOperationService.Extras.ACTION, OperationService.Action.START);
		intent.putExtra(TestOperationService.Extras.OPERATION, new TestOperation(null, null) {

			@Override
			public void execute() {
				super.execute();
				latch.execute();
			}
		});
		startService(intent);
		latch.assertComplete();
	}

    public void testOperationServiceCancelsOperation() {
        final OperationCancelCounter latch = new OperationCancelCounter(1);

        TestOperation testOperation = new TestOperation(null, null){
            @Override
            public void cancel() {
                super.cancel();
                latch.cancel();
            }
        };

        final Intent startIntent = new Intent(getContext(), TestOperationService.class);
        startIntent.putExtra(TestOperationService.Extras.ACTION, OperationService.Action.START);
        startIntent.putExtra(TestOperationService.Extras.OPERATION, testOperation);
        startService(startIntent);

        final Intent cancelIntent = new Intent(getContext(), TestOperationService.class);
        cancelIntent.putExtra(TestOperationService.Extras.ACTION, OperationService.Action.CANCEL);
        cancelIntent.putExtra(TestOperationService.Extras.OPERATION, testOperation);

        startService(cancelIntent);

        latch.assertComplete();
    }

	// =============================================

	public void testOperationServiceStartWithOperation() {
		final ServiceStartCounter latch = new ServiceStartCounter(1);
		final ContextWrapper context = new ContextWrapper(getContext()) {

			@Override
			public ComponentName startService(final Intent service) {
				latch.startService();
				assertEquals(OperationService.Action.START, service.getSerializableExtra(TestOperationService.Extras.ACTION));
				assertTrue(service.hasExtra(TestOperationService.Extras.OPERATION));
				return null;
			}
		};
		OperationService.start(context, TestOperationFactory.newOperationWithoutTasks());
		latch.assertComplete();
	}

	public void testOperationServiceCancelWithOperation() {
		final ServiceStartCounter latch = new ServiceStartCounter(1);
		final ContextWrapper context = new ContextWrapper(getContext()) {

			@Override
			public ComponentName startService(final Intent service) {
				latch.startService();
				assertEquals(OperationService.Action.CANCEL, service.getSerializableExtra(TestOperationService.Extras.ACTION));
				assertTrue(service.hasExtra(TestOperationService.Extras.OPERATION));
				return null;
			}
		};
		OperationService.cancel(context, TestOperationFactory.newOperationWithoutTasks());
		latch.assertComplete();
	}

	// =============================================

	private static class ServiceStartCounter {

		final AssertionLatch mStartLatch;

		public ServiceStartCounter(final int count) {
			mStartLatch = new AssertionLatch(count);
		}

		public void startService() {
			mStartLatch.countDown();
		}

		public void assertComplete() {
			mStartLatch.assertComplete();
		}
	}

    private static class OperationExecuteCounter {

        final AssertionLatch mExecuteLatch;

        public OperationExecuteCounter(final int count) {
            mExecuteLatch = new AssertionLatch(count);
        }

        public void execute() {
            mExecuteLatch.countDown();
        }

        public void assertComplete() {
            mExecuteLatch.assertComplete();
        }
    }

    private static class OperationCancelCounter {

        final AssertionLatch mCancelLatch;

        public OperationCancelCounter(final int count) {
            mCancelLatch = new AssertionLatch(count);
        }

        public void cancel() {
            mCancelLatch.countDown();
        }

        public void assertComplete() {
            mCancelLatch.assertComplete();
        }
    }
}
