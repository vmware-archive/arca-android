package com.xtreme.rest.service.test.mock;

import com.xtreme.rest.service.ProcessingRequestPrioritizable;
import com.xtreme.threading.RequestIdentifier;

public class TestProcessingPrioritizable extends ProcessingRequestPrioritizable<String> {

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
