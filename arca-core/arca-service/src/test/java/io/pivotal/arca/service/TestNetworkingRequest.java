package io.pivotal.arca.service;

public class TestNetworkingRequest extends NetworkingRequest<String> {

	public TestNetworkingRequest(final NetworkingPrioritizable<String> prioritizable) {
		super(prioritizable, 0, null);
	}

	@Override
	public void notifyComplete(final Object data, final ServiceError error) {

	}

}
