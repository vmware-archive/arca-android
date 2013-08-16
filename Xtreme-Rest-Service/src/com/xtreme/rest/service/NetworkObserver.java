package com.xtreme.rest.service;

public interface NetworkObserver<T> {
	public void onNetworkRequestComplete(T data);
	public void onNetworkRequestFailure(ServiceError error);
}
