package io.pivotal.arca.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

import io.pivotal.arca.threading.Identifier;

public class SimpleOperationTest extends AndroidTestCase {

    private static final String ERROR = "error";

    private static final String AUTHORITY = "authority";

    private static final Uri URI = Uri.parse("content://" + AUTHORITY + "/test");

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSimpleOperationCreatesIdentifierFromUri() {
        final Uri uri = Uri.parse("http://uri");
        final TestSimpleOperation operation = new TestSimpleOperation(uri);
        final Identifier<?> identifier = operation.onCreateIdentifier();
        assertEquals(uri, identifier.getData());
    }

    public void testSimpleOperationSucceeds() {
        final AssertionLatch latch = new AssertionLatch(1);
        final TestSimpleOperation operation = new TestSimpleOperation(URI);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
        operation.setContext(new MockContextWithProvider(new MockContentProvider() {
            @Override
            public int bulkInsert(Uri uri, ContentValues[] values) {
                return 0;
            }
        }));
        operation.setOperationObserver(new OperationObserver() {
            @Override
            public void onOperationComplete(final Operation o) {
                latch.countDown();

                assertNull(o.getError());
            }
        });
        operation.execute();
        latch.assertComplete();
    }

    public void testSimpleOperationFailsWithNetworkingError() {
        final AssertionLatch latch = new AssertionLatch(1);
        final Exception exception = new Exception(ERROR);
        final TestSimpleOperation operation = new TestSimpleOperation(exception, null);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
        operation.setContext(new MockContextWithProvider(null));
        operation.setOperationObserver(new OperationObserver() {
            @Override
            public void onOperationComplete(final Operation o) {
                latch.countDown();

                assertEquals(ERROR, o.getError().getMessage());
            }
        });
        operation.execute();
        latch.assertComplete();
    }

    public void testSimpleOperationFailsWithCustomNetworkingError() {
        final AssertionLatch latch = new AssertionLatch(1);
        final ServiceError error = new ServiceError(ERROR);
        final Exception exception = new ServiceException(error);
        final TestSimpleOperation operation = new TestSimpleOperation(exception, null);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
        operation.setContext(new MockContextWithProvider(null));
        operation.setOperationObserver(new OperationObserver() {
            @Override
            public void onOperationComplete(final Operation o) {
                latch.countDown();

                assertEquals(error, o.getError());
            }
        });
        operation.execute();
        latch.assertComplete();
    }

    public void testSimpleOperationFailsWithProcessingError() {
        final AssertionLatch latch = new AssertionLatch(1);
        final Exception exception = new Exception(ERROR);
        final TestSimpleOperation operation = new TestSimpleOperation(null, exception);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
        operation.setContext(new MockContextWithProvider(null));
        operation.setOperationObserver(new OperationObserver() {
            @Override
            public void onOperationComplete(final Operation o) {
                latch.countDown();

                assertEquals(ERROR, o.getError().getMessage());
            }
        });
        operation.execute();
        latch.assertComplete();
    }

    public void testSimpleOperationFailsWithCustomProcessingError() {
        final AssertionLatch latch = new AssertionLatch(1);
        final ServiceError error = new ServiceError(ERROR);
        final Exception exception = new ServiceException(error);
        final TestSimpleOperation operation = new TestSimpleOperation(null, exception);
        operation.setRequestExecutor(new RequestExecutor.SerialRequestExecutor());
        operation.setContext(new MockContextWithProvider(null));
        operation.setOperationObserver(new OperationObserver() {
            @Override
            public void onOperationComplete(final Operation o) {
                latch.countDown();

                assertEquals(error, o.getError());
            }
        });
        operation.execute();
        latch.assertComplete();
    }

    public void testSimpleOperationNotifiesChangeOnSuccess() {
        final AssertionLatch latch = new AssertionLatch(1);
        final MockContext context = new MockContext() {
            @Override
            public ContentResolver getContentResolver() {
                return new MockContentResolver() {
                    @Override
                    public void notifyChange(final Uri u, final ContentObserver o) {
                        latch.countDown();
                    }
                };
            }
        };
        final TestSimpleOperation operation = new TestSimpleOperation(Uri.EMPTY);
        operation.onSuccess(context, null);
        latch.assertComplete();
    }

    public void testSimpleOperationInsertsDataOnSuccess() throws Exception {
        final AssertionLatch latch = new AssertionLatch(1);
        final MockContext context = new MockContextWithProvider(new MockContentProvider() {
            @Override
            public int bulkInsert(Uri u, ContentValues[] v) {
                latch.countDown();
                assertEquals(URI, u);
                return 0;
            }
        });
        final TestSimpleOperation operation = new TestSimpleOperation(URI);
        operation.onPostExecute(context, new ContentValues[0]);
        latch.assertComplete();
    }


    // =============================================


    private static class MockContextWithProvider extends MockContext {

        private final MockContentProvider mProvider;

        public MockContextWithProvider(final MockContentProvider provider) {
            mProvider = provider;
        }

        @Override
        public ContentResolver getContentResolver() {
            final MockContentResolver resolver = new MockContentResolver();
            resolver.addProvider(AUTHORITY, mProvider);
            return resolver;
        }
    }

}
