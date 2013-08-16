package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.RequestIdentifier;

public class ProcessingPrioritizable<T> extends Prioritizable {

	private final ProcessingRequester<T> mRequester;
	private final T mData;

	private ServiceError mError;

	public ProcessingPrioritizable(final ProcessingRequester<T> requester, final T data) {
		mRequester = requester;
		mData = data;
	}

	@Override
	public RequestIdentifier<?> getIdentifier() {
		return mRequester.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mRequester.executeProcessingRequest(mData);
		} catch (final ServiceException e) {
			Logger.ex(e);
			mError = e.getError();
		} catch (final Exception e) {
			Logger.ex(e);
			mError = new ServiceError(e);
		}
	}

	public void onComplete(final ServiceError error) {
		if (error == null) {
			mRequester.onProcessingRequestComplete();
		} else {
			mRequester.onProcessingRequestFailure(error);
		}
	}
	
	public Object getData() {
		return mData;
	}

	public ServiceError getError() {
		return mError;
	}

}