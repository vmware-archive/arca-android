package com.xtreme.rest.service;

public interface ProcessingRequestObserver<T> {
	public void onProcessingRequestComplete();
	public void onProcessingRequestFailure(ServiceError error);
}
