package io.pivotal.arca.service;

import io.pivotal.arca.threading.Identifier;

public interface ProcessingTask<T> {
	Identifier<?> getIdentifier();

	void executeProcessing(T data) throws Exception;
}
