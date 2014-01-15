package com.xtreme.rest.service;

import com.xtreme.threading.RequestIdentifier;

public interface NetworkRequestExecutor<T> {
	public RequestIdentifier<?> getIdentifier();
	public T executeNetworkRequest() throws Exception;
}
