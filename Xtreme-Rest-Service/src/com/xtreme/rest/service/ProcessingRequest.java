package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.PrioritizableRequest;

public class ProcessingRequest<T> extends PrioritizableRequest {

	private final ProcessingObserver<T> mListener;
	
	public ProcessingRequest(final Prioritizable prioritizable, final int accessorIndex, final ProcessingObserver<T> listener) {
		super(prioritizable, accessorIndex);
		mListener = listener;
	}
	
	public void notifyComplete(final ServiceError error) {
		if (error == null) {
			mListener.onProcessingRequestComplete();
		} else {
			mListener.onProcessingRequestFailure(error);
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
