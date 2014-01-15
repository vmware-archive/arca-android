package com.xtreme.rest.service;

public interface NetworkRequestObserver<T> {
	public void onNetworkRequestComplete(T data);
	public void onNetworkRequestFailure(ServiceError error);
}
