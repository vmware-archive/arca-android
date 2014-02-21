package com.arca.service;

public interface NetworkingPrioritizableObserver<T> {
	public void onNetworkingComplete(T data);
	public void onNetworkingFailure(ServiceError error);
}
