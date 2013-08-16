package com.xtreme.rest.service.test.junit.mock;

import com.xtreme.rest.service.NetworkPrioritizable;
import com.xtreme.rest.service.PrioritizableHandler;
import com.xtreme.rest.service.ProcessingPrioritizable;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.threading.PrioritizableRequest;

public class TestPrioritizableHandler implements PrioritizableHandler {

	@Override
	public void executeNetworkComponent(final PrioritizableRequest request) {
		
		// Tell the request to run synchronously
		request.run();
		
		final NetworkPrioritizable<?> result = (NetworkPrioritizable<?>) request.getPrioritizable();
		
		final ServiceError error = result.getError();
		final Object object = result.getData();
		
		result.onComplete(object, error);
	}

	@Override
	public void executeProcessingComponent(final PrioritizableRequest request) {

		// Tell the request to run synchronously
		request.run();
		
		final ProcessingPrioritizable<?> result = (ProcessingPrioritizable<?>) request.getPrioritizable();
		
		final ServiceError error = result.getError();
		
		result.onComplete(error);
	}

}
