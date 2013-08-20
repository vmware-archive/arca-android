package com.xtreme.rest.service;

import com.xtreme.threading.RequestIdentifier;

public interface ProcessingHandler<T> {
	public RequestIdentifier<?> getIdentifier();
	public void executeProcessingRequest(T data) throws Exception;
}