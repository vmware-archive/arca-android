package com.xtreme.rest.service;

import com.xtreme.threading.RequestIdentifier;

public interface ProcessingRequester<T> {
	public RequestIdentifier<?> getIdentifier();
	public void executeProcessingRequest(T data) throws Exception;
	public void onProcessingRequestComplete();
	public void onProcessingRequestFailure(ServiceError error);
}
