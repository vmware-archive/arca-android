package com.arca.service;

import com.arca.threading.RequestIdentifier;


public interface NetworkingTask<T> {
	public RequestIdentifier<?> getIdentifier();
	public T executeNetworking() throws Exception;
}
