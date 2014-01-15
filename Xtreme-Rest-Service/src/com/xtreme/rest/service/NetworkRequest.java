package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.PrioritizableRequest;

public class NetworkRequest<T> extends PrioritizableRequest {

	private final NetworkRequestObserver<T> mObserver;
	
	public NetworkRequest(final Prioritizable prioritizable, final int accessorIndex, final NetworkRequestObserver<T> observer) {
		super(prioritizable, accessorIndex);
		mObserver = observer;
	}
	
	@SuppressWarnings("unchecked")
	public void notifyComplete(final Object data, final ServiceError error) {
		if (error == null) {
			mObserver.onNetworkRequestComplete((T) data);
		} else {
			mObserver.onNetworkRequestFailure(error);
		}
	}

	@Override
	public NetworkRequestPrioritizable<?> getPrioritizable() {
		return (NetworkRequestPrioritizable<?>) super.getPrioritizable();
	}
	
	public Object getData() {
		return getPrioritizable().getData();
	}

	public ServiceError getError() {
		return getPrioritizable().getError();
	}
}
