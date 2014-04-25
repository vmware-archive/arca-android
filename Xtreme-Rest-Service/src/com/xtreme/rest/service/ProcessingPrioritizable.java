package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.RequestIdentifier;

class ProcessingPrioritizable<T> extends Prioritizable {

	private final Task<T> mTask;
	private final T mData;

	private ServiceError mError;

	ProcessingPrioritizable(final Task<T> task, final T data) {
		mTask = task;
		mData = data;
	}

	@Override
	public RequestIdentifier<?> getIdentifier() {
		return mTask.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mTask.startProcessingRequest(mData);
		} catch (final ServiceException e) {
			Logger.ex(e);
			mError = e.getError();
		} catch (final Exception e) {
			Logger.ex(e);
			mError = new ServiceError(e);
		}
	}

	void onComplete(final ServiceError error) {
		if (error == null) {
			mTask.onProcessingRequestComplete();
		} else {
			mTask.onProcessingRequestFailure(error);
		}
	}

	ServiceError getError() {
		return mError;
	}

}