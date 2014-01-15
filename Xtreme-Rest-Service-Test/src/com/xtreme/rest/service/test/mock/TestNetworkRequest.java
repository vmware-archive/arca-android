package com.xtreme.rest.service.test.mock;

import com.xtreme.rest.service.NetworkRequest;
import com.xtreme.rest.service.NetworkRequestPrioritizable;
import com.xtreme.rest.service.ServiceError;

public class TestNetworkRequest extends NetworkRequest<String> {

	public TestNetworkRequest(final NetworkRequestPrioritizable<String> prioritizable) {
		super(prioritizable, 0, null);
	}
	
	@Override
	public void notifyComplete(final Object data, final ServiceError error) {
		
	}

}
