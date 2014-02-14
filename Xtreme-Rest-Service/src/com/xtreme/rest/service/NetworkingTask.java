package com.xtreme.rest.service;

import com.xtreme.threading.RequestIdentifier;

public interface NetworkingTask<T> {
	public RequestIdentifier<?> getIdentifier();
	public T executeNetworking() throws Exception;
}
