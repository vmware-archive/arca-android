package io.pivotal.arca.service;

public interface ProcessingPrioritizableObserver<T> {
	public void onProcessingComplete();

	public void onProcessingFailure(ServiceError error);
}
