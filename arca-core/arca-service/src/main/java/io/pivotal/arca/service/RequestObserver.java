package io.pivotal.arca.service;

public interface RequestObserver {
	void onNetworkingRequestComplete(NetworkingRequest<?> request);

	void onNetworkingRequestCancelled(NetworkingRequest<?> request);

	void onProcessingRequestComplete(ProcessingRequest<?> request);

	void onProcessingRequestCancelled(ProcessingRequest<?> request);
}
