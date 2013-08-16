package com.xtreme.rest.service;

import com.xtreme.threading.RequestIdentifier;

public interface NetworkRequester<T> {
	public RequestIdentifier<?> getIdentifier();
	public T executeNetworkRequest() throws Exception;
	public void onNetworkRequestComplete(T data);
	public void onNetworkRequestFailure(ServiceError error);
}
