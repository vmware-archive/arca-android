package com.xtreme.rest.service.test.junit.mock;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.OperationObserver;
import com.xtreme.rest.service.RequestHandler;
import com.xtreme.rest.service.RestService;

public class TestRestService extends RestService {
	
	public static class Extras extends RestService.Extras {}
	
	
	@Override
	protected OperationObserver onCreateOperationObserver() {
		return new TestOperationHandler(this);
	}
	
	@Override
	protected RequestHandler onCreateRequestHandler() {
		return new TestRequestHandler();
	}
	
	public void addOperation(final Operation operation) {
		getOperations().add(operation);
	}
	
}
