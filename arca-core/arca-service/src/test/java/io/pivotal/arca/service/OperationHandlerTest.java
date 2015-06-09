/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import android.net.Uri;
import android.test.AndroidTestCase;

public class OperationHandlerTest extends AndroidTestCase {

    public void testOperationServiceDoesntExecuteAlreadyRunningOperation() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation = new TestOperation(null, null);
        handler.getOperations().put(operation.getUri(), operation);

        assertFalse(handler.start(operation));
    }

    public void testOperationServiceDoesntCancelNonStartedOperation() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation = new TestOperation(null, null);

        assertFalse(handler.cancel(operation));
    }

    public void testOperationServiceCancelsAlreadyRunningOperation() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation = new TestOperation(null, null);
        handler.getOperations().put(operation.getUri(), operation);

        assertTrue(handler.cancel(operation));
    }

    public void testOperationServiceShutsDownWhenAllOperationsFinish() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation = new TestOperation(null, null);
        handler.getOperations().put(operation.getUri(), operation);

        assertTrue(handler.finish(operation));
    }

    public void testOperationServiceDoesntShutDownWhenMoreOperationsRunning() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation1 = new TestOperation(Uri.parse("operation1"), null);
        final TestOperation operation2 = new TestOperation(Uri.parse("operation2"), null);
        handler.getOperations().put(operation1.getUri(), operation1);
        handler.getOperations().put(operation2.getUri(), operation2);

        assertFalse(handler.finish(operation1));
        assertTrue(handler.finish(operation2));
    }
}
