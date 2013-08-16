package com.xtreme.rest.service;

public interface ProcessingObserver<T> {
	public void onProcessingRequestComplete();
	public void onProcessingRequestFailure(ServiceError error);
}
