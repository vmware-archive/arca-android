package com.xtreme.rest.service.test.junit.mock;

import com.xtreme.rest.service.NetworkRequest;
import com.xtreme.rest.service.ProcessingRequest;
import com.xtreme.rest.service.RequestHandler;
import com.xtreme.rest.service.ServiceError;

public class TestRequestHandler implements RequestHandler {

	@Override
	public void executeNetworkRequest(final NetworkRequest<?> request) {
		
		// Tell the request to run synchronously
		request.run();
		
		final ServiceError error = request.getError();
		final Object object = request.getData();
		
		// Notify Immediately
		request.notifyComplete(object, error);
	}

	@Override
	public void executeProcessingRequest(final ProcessingRequest<?> request) {

		// Tell the request to run synchronously
		request.run();
		
		final ServiceError error = request.getError();
		
		// Notify Immediately
		request.notifyComplete(error);
	}

}
