package io.pivotal.arca.service;

public interface NetworkingPrioritizableObserver<T> {
	void onNetworkingComplete(T data);

	void onNetworkingFailure(ServiceError error);
}
