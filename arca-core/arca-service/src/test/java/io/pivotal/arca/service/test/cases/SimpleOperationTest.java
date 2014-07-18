package io.pivotal.arca.service.test.cases;

import android.test.AndroidTestCase;

import io.pivotal.arca.service.Operation;
import io.pivotal.arca.service.OperationObserver;
import io.pivotal.arca.service.RequestExecutor;
import io.pivotal.arca.service.ServiceError;
import io.pivotal.arca.service.ServiceException;
import io.pivotal.arca.service.test.mock.TestSimpleOperation;
import io.pivotal.arca.service.test.utils.AssertionLatch;

public class SimpleOperationTest extends AndroidTestCase {

    private static final String ERROR = "error";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSimpleOperationSucceeds() {
        final String data = "test_data";
        final ObserverCounter latch = new ObserverCounter(1);
        final TestSimpleOperation operation = new TestSimpleOperation(data);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
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

    public void testSimpleOperationFailsWithNetworkingError() {
        final ObserverCounter latch = new ObserverCounter(1);
        final Exception exception = new Exception(ERROR);
        final TestSimpleOperation operation = new TestSimpleOperation(exception, null);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
        operation.setOperationObserver(new OperationObserver() {
            @Override
            public void onOperationComplete(final Operation o) {
                latch.onOperationComplete();

                assertEquals(ERROR, o.getError().getMessage());
            }
        });
        operation.execute();
        latch.assertComplete();
    }

    public void testSimpleOperationFailsWithCustomNetworkingError() {
        final ObserverCounter latch = new ObserverCounter(1);
        final ServiceError error = new ServiceError(ERROR);
        final Exception exception = new ServiceException(error);
        final TestSimpleOperation operation = new TestSimpleOperation(exception, null);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
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

    public void testSimpleOperationFailsWithProcessingError() {
        final ObserverCounter latch = new ObserverCounter(1);
        final Exception exception = new Exception(ERROR);
        final TestSimpleOperation operation = new TestSimpleOperation(null, exception);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
        operation.setOperationObserver(new OperationObserver() {
            @Override
            public void onOperationComplete(final Operation o) {
                latch.onOperationComplete();

                assertEquals(ERROR, o.getError().getMessage());
            }
        });
        operation.execute();
        latch.assertComplete();
    }

    public void testSimpleOperationFailsWithCustomProcessingError() {
        final ObserverCounter latch = new ObserverCounter(1);
        final ServiceError error = new ServiceError(ERROR);
        final Exception exception = new ServiceException(error);
        final TestSimpleOperation operation = new TestSimpleOperation(null, exception);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
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
