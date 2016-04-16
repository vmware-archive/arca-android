package io.pivotal.arca.service;

public class TestProcessingRequest extends ProcessingRequest<String> {

	public TestProcessingRequest(final ProcessingPrioritizable<String> prioritizable) {
		super(prioritizable, 0, null);
	}

	@Override
	public void notifyComplete(final ServiceError error) {

	}

}
