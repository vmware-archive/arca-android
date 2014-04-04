package com.arca.service;

import com.arca.threading.Identifier;


public interface NetworkingTask<T> {
	public Identifier<?> getIdentifier();
	public T executeNetworking() throws Exception;
}
