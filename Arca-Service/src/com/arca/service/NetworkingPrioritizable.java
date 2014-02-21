package com.arca.service;

import com.arca.utils.Logger;
import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.RequestIdentifier;

public class NetworkingPrioritizable<T> extends Prioritizable {

	private final NetworkingTask<T> mTask;

	private ServiceError mError;
	private T mData;

	public NetworkingPrioritizable(final NetworkingTask<T> task) {
		mTask = task;
	}

	@Override
	public RequestIdentifier<?> getIdentifier() {
		return mTask.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mData = mTask.executeNetworking();
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