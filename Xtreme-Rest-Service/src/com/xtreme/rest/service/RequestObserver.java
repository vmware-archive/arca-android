package com.xtreme.rest.service;

public interface RequestObserver {
	public void onNetworkRequestComplete(NetworkRequest<?> request);
	public void onProcessingRequestComplete(ProcessingRequest<?> request);
}
