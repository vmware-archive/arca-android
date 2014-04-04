package com.arca.service.test.mock;

import com.arca.service.NetworkingPrioritizable;
import com.arca.threading.Identifier;

public class TestNetworkingPrioritizable extends NetworkingPrioritizable<String> {

	public TestNetworkingPrioritizable() {
		super(null);
	}
	
	@Override
	public Identifier<?> getIdentifier() {
		return new Identifier<String>("empty");
	}

	@Override
	public void execute() {
		// do nothing
	}

}
