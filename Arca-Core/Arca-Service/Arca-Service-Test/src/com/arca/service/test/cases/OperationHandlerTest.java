package com.arca.service.test.cases;

import android.test.AndroidTestCase;

import com.arca.service.OperationHandler;
import com.arca.service.test.mock.TestOperation;

public class OperationHandlerTest extends AndroidTestCase {

	
    public void testOperationServiceDoesntExecuteAlreadyRunningOperation() {
    	final OperationHandler handler = new OperationHandler(null, null);
		final TestOperation operation = new TestOperation(null, null);
		handler.getOperations().add(operation);
		assertFalse(handler.start(operation));
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
	
}
