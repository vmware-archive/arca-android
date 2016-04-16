package io.pivotal.arca.service;

import io.pivotal.arca.threading.Identifier;

public interface ProcessingTask<T> {
	public Identifier<?> getIdentifier();

	public void executeProcessing(T data) throws Exception;
}
