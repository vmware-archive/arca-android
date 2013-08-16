package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.PrioritizableRequest;

public class NetworkRequest<T> extends PrioritizableRequest {

	private final NetworkObserver<T> mListener;
	
	public NetworkRequest(final Prioritizable prioritizable, final int accessorIndex, final NetworkObserver<T> listener) {
		super(prioritizable, accessorIndex);
		mListener = listener;
	}
	
	@SuppressWarnings("unchecked")
	public void notifyComplete(final Object data, final ServiceError error) {
		if (error == null) {
			mListener.onNetworkRequestComplete((T) data);
		} else {
			mListener.onNetworkRequestFailure(error);
		}
	}

	@Override
	public NetworkPrioritizable<?> getPrioritizable() {
		return (NetworkPrioritizable<?>) super.getPrioritizable();
	}
	
	public Object getData() {
		return getPrioritizable().getData();
	}

	public ServiceError getError() {
		return getPrioritizable().getError();
	}
}
