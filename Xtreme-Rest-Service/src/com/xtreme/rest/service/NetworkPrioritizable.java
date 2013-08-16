package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.RequestIdentifier;

public class NetworkPrioritizable<T> extends Prioritizable {

	private final NetworkHandler<T> mHandler;

	private ServiceError mError;
	private T mData;

	public NetworkPrioritizable(final NetworkHandler<T> handler) {
		mHandler = handler;
	}

	@Override
	public RequestIdentifier<?> getIdentifier() {
		return mHandler.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mData = mHandler.executeNetworkRequest();
		} catch (final ServiceException e) {
			Logger.ex(e);
			mError = e.getError();
		} catch (final Exception e) {
			Logger.ex(e);
			mError = new ServiceError(e);
		}
	}

	public Object getData() {
		return mData;
	}

	public ServiceError getError() {
		return mError;
	}

}