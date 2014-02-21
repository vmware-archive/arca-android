package com.arca.service.test.mock;

import com.arca.service.ProcessingPrioritizable;
import com.xtreme.threading.RequestIdentifier;

public class TestProcessingPrioritizable extends ProcessingPrioritizable<String> {

	public TestProcessingPrioritizable() {
		super(null, null);
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
