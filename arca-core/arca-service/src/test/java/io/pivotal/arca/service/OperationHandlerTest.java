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

import android.os.Message;
import android.test.AndroidTestCase;

public class OperationHandlerTest extends AndroidTestCase {

    public void testOperationServiceDoesntExecuteAlreadyRunningOperation() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation = new TestOperation(null, null);
        handler.getOperations().add(operation);
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
        handler.getOperations().add(operation);

        assertTrue(handler.cancel(operation));
    }

    public void testOperationServiceShutsDownWhenAllOperationsFinish() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation = new TestOperation(null, null);
        handler.getOperations().add(operation);
        assertTrue(handler.finish(operation));
    }

    public void testOperationServiceDoesntShutDownWhenMoreOperationsRunning() {
        final OperationHandler handler = new OperationHandler(null, null);
        final TestOperation operation1 = new TestOperation(null, null);
        final TestOperation operation2 = new TestOperation(null, null);
        handler.getOperations().add(operation1);
        handler.getOperations().add(operation2);
        assertFalse(handler.finish(operation1));
        assertTrue(handler.finish(operation2));
    }

    public void testOperationServiceShutsDownWhenOperationsAreCancelled() {
        final BooleanResultObserver finishResultObserver = new BooleanResultObserver();

        final OperationHandler handler = new OperationHandler(null, null) {
            @Override
            public boolean finish(Operation operation) {
                boolean result = super.finish(operation);
                finishResultObserver.setResult(result);
                return result;
            }

            @Override
            public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
                handleMessage(msg);
                return true;
            }
        };

        final TestOperation operation1 = new TestOperation(null, null);
        final TestOperation operation2 = new TestOperation(null, null);

        handler.getOperations().add(operation1);
        handler.getOperations().add(operation2);
        operation1.setOperationObserver(handler);
        operation2.setOperationObserver(handler);

        assertTrue(handler.cancel(operation1));
        assertFalse(finishResultObserver.getResult());

        assertTrue(handler.cancel(operation2));
        assertTrue(finishResultObserver.getResult());
    }

    public static class BooleanResultObserver {
        private Boolean mResult = null;

        public void setResult(boolean newResult) {
            mResult = newResult;
        }

        public Boolean getResult() {
            return mResult;
        }
    }
}
