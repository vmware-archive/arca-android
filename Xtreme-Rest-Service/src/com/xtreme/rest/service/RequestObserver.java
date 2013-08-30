package com.xtreme.rest.service;

public interface RequestObserver {
	public void onNetworkRequestComplete(NetworkRequest<?> request);
	public void onNetworkRequestCancelled(NetworkRequest<?> request);
	public void onProcessingRequestComplete(ProcessingRequest<?> request);
	public void onProcessingRequestCancelled(ProcessingRequest<?> request);
}
