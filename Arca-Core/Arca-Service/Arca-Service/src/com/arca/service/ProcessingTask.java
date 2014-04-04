package com.arca.service;

import com.arca.threading.Identifier;


public interface ProcessingTask<T> {
	public Identifier<?> getIdentifier();
	public void executeProcessing(T data) throws Exception;
}
