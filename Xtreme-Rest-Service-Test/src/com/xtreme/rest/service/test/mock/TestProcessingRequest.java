package com.xtreme.rest.service.test.mock;

import com.xtreme.rest.service.ProcessingPrioritizable;
import com.xtreme.rest.service.ProcessingRequest;
import com.xtreme.rest.service.ServiceError;

public class TestProcessingRequest extends ProcessingRequest<String> {

	public TestProcessingRequest(final ProcessingPrioritizable<String> prioritizable) {
		super(prioritizable, 0, null);
	}
	
	@Override
	public void notifyComplete(final ServiceError error) {
		
	}

}
