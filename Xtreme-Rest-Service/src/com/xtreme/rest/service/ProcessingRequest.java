package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.PrioritizableRequest;

public class ProcessingRequest<T> extends PrioritizableRequest {

	private final ProcessingObserver<T> mObserver;
	
	public ProcessingRequest(final Prioritizable prioritizable, final int accessorIndex, final ProcessingObserver<T> observer) {
		super(prioritizable, accessorIndex);
		mObserver = observer;
	}
	
	public void notifyComplete(final ServiceError error) {
		if (error == null) {
			mObserver.onProcessingRequestComplete();
		} else {
			mObserver.onProcessingRequestFailure(error);
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
