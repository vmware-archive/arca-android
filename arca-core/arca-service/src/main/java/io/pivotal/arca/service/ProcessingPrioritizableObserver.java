package io.pivotal.arca.service;

public interface ProcessingPrioritizableObserver<T> {
	void onProcessingComplete();

	void onProcessingFailure(ServiceError error);
}
