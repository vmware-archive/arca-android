package com.xtreme.rest.service;

import com.xtreme.threading.RequestIdentifier;

public interface NetworkHandler<T> {
	public RequestIdentifier<?> getIdentifier();
	public T executeNetworkRequest() throws Exception;
}
