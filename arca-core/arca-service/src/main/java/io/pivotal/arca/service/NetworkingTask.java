package io.pivotal.arca.service;

import io.pivotal.arca.threading.Identifier;

public interface NetworkingTask<T> {
	public Identifier<?> getIdentifier();

	public T executeNetworking() throws Exception;
}
