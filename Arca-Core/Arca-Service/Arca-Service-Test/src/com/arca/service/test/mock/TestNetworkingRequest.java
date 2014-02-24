package com.arca.service.test.mock;

import com.arca.service.NetworkingPrioritizable;
import com.arca.service.NetworkingRequest;
import com.arca.service.ServiceError;

public class TestNetworkingRequest extends NetworkingRequest<String> {

	public TestNetworkingRequest(final NetworkingPrioritizable<String> prioritizable) {
		super(prioritizable, 0, null);
	}
	
	@Override
	public void notifyComplete(final Object data, final ServiceError error) {
		
	}

}
