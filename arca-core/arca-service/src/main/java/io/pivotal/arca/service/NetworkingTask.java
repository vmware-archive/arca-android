package io.pivotal.arca.service;

import io.pivotal.arca.threading.Identifier;

public interface NetworkingTask<T> {
	Identifier<?> getIdentifier();

	T executeNetworking() throws Exception;
}
