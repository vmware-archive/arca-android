package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.RequestIdentifier;

class NetworkPrioritizable<T> extends Prioritizable {

	private final Task<T> mTask;

	private ServiceError mError;
	private T mData;

	NetworkPrioritizable(final Task<T> task) {
		mTask = task;
	}

	@Override
	public RequestIdentifier<?> getIdentifier() {
		return mTask.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mData = mTask.startNetworkRequest();
		} catch (final ServiceException e) {
			Logger.ex(e);
			mError = e.getError();
		} catch (final Exception e) {
			Logger.ex(e);
			mError = new ServiceError(e);
		}
	}

	@SuppressWarnings("unchecked")
	void onComplete(final Object data, final ServiceError error) {
		if (error == null) {
			mTask.onNetworkRequestComplete((T) data);
		} else {
			mTask.onNetworkRequestFailure(error);
		}
	}

	Object getData() {
		return mData;
	}

	ServiceError getError() {
		return mError;
	}

}