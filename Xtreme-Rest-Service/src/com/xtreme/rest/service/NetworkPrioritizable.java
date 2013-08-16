package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.RequestIdentifier;

public class NetworkPrioritizable<T> extends Prioritizable {

	private final NetworkRequester<T> mRequester;

	private ServiceError mError;
	private T mData;

	public NetworkPrioritizable(final NetworkRequester<T> requester) {
		mRequester = requester;
	}

	@Override
	public RequestIdentifier<?> getIdentifier() {
		return mRequester.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mData = mRequester.executeNetworkRequest();
		} catch (final ServiceException e) {
			Logger.ex(e);
			mError = e.getError();
		} catch (final Exception e) {
			Logger.ex(e);
			mError = new ServiceError(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void onComplete(final Object data, final ServiceError error) {
		if (error == null) {
			mRequester.onNetworkRequestComplete((T) data);
		} else {
			mRequester.onNetworkRequestFailure(error);
		}
	}

	public Object getData() {
		return mData;
	}

	public ServiceError getError() {
		return mError;
	}

}