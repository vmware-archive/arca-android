package com.arca.service;

import com.xtreme.threading.RequestIdentifier;

public interface ProcessingTask<T> {
	public RequestIdentifier<?> getIdentifier();
	public void executeProcessing(T data) throws Exception;
}
