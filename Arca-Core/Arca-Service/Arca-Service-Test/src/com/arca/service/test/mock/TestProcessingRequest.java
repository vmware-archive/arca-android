package com.arca.service.test.mock;

import com.arca.service.ProcessingPrioritizable;
import com.arca.service.ProcessingRequest;
import com.arca.service.ServiceError;

public class TestProcessingRequest extends ProcessingRequest<String> {

	public TestProcessingRequest(final ProcessingPrioritizable<String> prioritizable) {
		super(prioritizable, 0, null);
	}
	
	@Override
	public void notifyComplete(final ServiceError error) {
		
	}

}
