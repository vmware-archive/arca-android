package com.arca.service.test.mock;

import com.arca.service.NetworkingPrioritizable;
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
