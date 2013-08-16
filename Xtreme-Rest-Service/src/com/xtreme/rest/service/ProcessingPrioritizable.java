package com.xtreme.rest.service;

import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.RequestIdentifier;

public class ProcessingPrioritizable<T> extends Prioritizable {

	private final ProcessingHandler<T> mHandler;
	private final T mData;

	private ServiceError mError;

	public ProcessingPrioritizable(final ProcessingHandler<T> handler, final T data) {
		mHandler = handler;
		mData = data;
	}

	@Override
	public RequestIdentifier<?> getIdentifier() {
		return mHandler.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mHandler.executeProcessingRequest(mData);
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