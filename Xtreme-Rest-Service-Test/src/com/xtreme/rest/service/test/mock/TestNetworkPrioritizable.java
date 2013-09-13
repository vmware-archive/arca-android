package com.xtreme.rest.service.test.mock;

import com.xtreme.rest.service.NetworkPrioritizable;
import com.xtreme.threading.RequestIdentifier;

public class TestNetworkPrioritizable extends NetworkPrioritizable<String> {

	public TestNetworkPrioritizable() {
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
