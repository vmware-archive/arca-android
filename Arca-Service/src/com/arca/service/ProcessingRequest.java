package com.arca.service;

import com.xtreme.threading.PrioritizableRequest;

public class ProcessingRequest<T> extends PrioritizableRequest {

	private final ProcessingPrioritizableObserver<T> mObserver;
	
	public ProcessingRequest(final ProcessingPrioritizable<?> prioritizable, final int accessorIndex, final ProcessingPrioritizableObserver<T> observer) {
		super(prioritizable, accessorIndex);
		mObserver = observer;
	}
	
	public void notifyComplete(final ServiceError error) {
		if (error == null) {
			mObserver.onProcessingComplete();
		} else {
			mObserver.onProcessingFailure(error);
		}
	}

	@Override
	public ProcessingPrioritizable<?> getPrioritizable() {
		return (ProcessingPrioritizable<?>) super.getPrioritizable();
	}
	
	public Object getData() {
		return getPrioritizable().getData();
	}
	
	public ServiceError getError() {
		return getPrioritizable().getError();
	}
	
}
