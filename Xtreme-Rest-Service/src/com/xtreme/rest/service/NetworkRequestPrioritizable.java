package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.RequestIdentifier;

public class NetworkRequestPrioritizable<T> extends Prioritizable {

	private final NetworkRequestExecutor<T> mExecutor;

	private ServiceError mError;
	private T mData;

	public NetworkRequestPrioritizable(final NetworkRequestExecutor<T> executor) {
		mExecutor = executor;
	}

	@Override
	public RequestIdentifier<?> getIdentifier() {
		return mExecutor.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mData = mExecutor.executeNetworkRequest();
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