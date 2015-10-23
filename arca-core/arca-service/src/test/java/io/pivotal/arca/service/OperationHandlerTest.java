/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import android.test.AndroidTestCase;

import io.pivotal.arca.threading.Identifier;

public class OperationHandlerTest extends AndroidTestCase {

    public void testOperationServiceDoesntExecuteAlreadyRunningOperation() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation = new TestOperation(null, null);
        handler.getOperations().put(operation.getIdentifier(), operation);

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
        handler.getOperations().put(operation.getIdentifier(), operation);

        assertTrue(handler.cancel(operation));
    }

    public void testOperationServiceShutsDownWhenAllOperationsFinish() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation = new TestOperation(null, null);
        handler.getOperations().put(operation.getIdentifier(), operation);

        assertTrue(handler.finish(operation));
    }

    public void testOperationServiceDoesntShutDownWhenMoreOperationsRunning() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation1 = new TestOperation(null, null) {
            @Override
            public Identifier<?> onCreateIdentifier() {
                return new Identifier<String>("operation1");
            }
        };
        final TestOperation operation2 = new TestOperation(null, null) {
            @Override
            public Identifier<?> onCreateIdentifier() {
                return new Identifier<String>("operation2");
            }
        };
        handler.getOperations().put(operation1.getIdentifier(), operation1);
        handler.getOperations().put(operation2.getIdentifier(), operation2);

        assertFalse(handler.finish(operation1));
        assertTrue(handler.finish(operation2));
    }
}
