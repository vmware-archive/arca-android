package com.xtreme.rest.service.test.mock;

import com.xtreme.rest.service.NetworkingPrioritizable;
import com.xtreme.rest.service.NetworkingRequest;
import com.xtreme.rest.service.ServiceError;

public class TestNetworkingRequest extends NetworkingRequest<String> {

	public TestNetworkingRequest(final NetworkingPrioritizable<String> prioritizable) {
		super(prioritizable, 0, null);
	}
	
	@Override
	public void notifyComplete(final Object data, final ServiceError error) {
		
	}

}
