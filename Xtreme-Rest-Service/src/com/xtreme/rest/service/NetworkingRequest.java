package com.xtreme.rest.service;

import com.xtreme.threading.PrioritizableRequest;

public class NetworkingRequest<T> extends PrioritizableRequest {

	private final NetworkingPrioritizableObserver<T> mObserver;
	
	public NetworkingRequest(final NetworkingPrioritizable<?> prioritizable, final int accessorIndex, final NetworkingPrioritizableObserver<T> observer) {
		super(prioritizable, accessorIndex);
		mObserver = observer;
	}
	
	@SuppressWarnings("unchecked")
	public void notifyComplete(final Object data, final ServiceError error) {
		if (error == null) {
			mObserver.onNetworkingComplete((T) data);
		} else {
			mObserver.onNetworkingFailure(error);
		}
	}

	@Override
	public NetworkingPrioritizable<?> getPrioritizable() {
		return (NetworkingPrioritizable<?>) super.getPrioritizable();
	}
	
	public Object getData() {
		return getPrioritizable().getData();
	}

	public ServiceError getError() {
		return getPrioritizable().getError();
	}
}
