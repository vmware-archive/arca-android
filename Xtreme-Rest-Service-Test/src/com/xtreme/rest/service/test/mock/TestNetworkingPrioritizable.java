package com.xtreme.rest.service.test.mock;

import com.xtreme.rest.service.NetworkingPrioritizable;
import com.xtreme.threading.RequestIdentifier;

public class TestNetworkingPrioritizable extends NetworkingPrioritizable<String> {

	public TestNetworkingPrioritizable() {
		super(null);
	}
	
	@Override
	public RequestIdentifier<?> getIdentifier() {
		return new RequestIdentifier<String>("empty");
	}

	@Override
	public void execute() {
		// do nothing
	}

}
