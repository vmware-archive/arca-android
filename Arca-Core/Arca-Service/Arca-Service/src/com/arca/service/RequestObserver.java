package com.arca.service;

public interface RequestObserver {
	public void onNetworkingRequestComplete(NetworkingRequest<?> request);
	public void onNetworkingRequestCancelled(NetworkingRequest<?> request);
	public void onProcessingRequestComplete(ProcessingRequest<?> request);
	public void onProcessingRequestCancelled(ProcessingRequest<?> request);
}
