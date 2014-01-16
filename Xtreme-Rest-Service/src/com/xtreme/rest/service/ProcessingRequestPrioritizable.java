package com.xtreme.rest.service;

import com.xtreme.rest.utils.Logger;
import com.xtreme.threading.Prioritizable;
import com.xtreme.threading.RequestIdentifier;

public class ProcessingRequestPrioritizable<T> extends Prioritizable {

	private final ProcessingRequestExecutor<T> mExecutor;
	private final T mData;

	private ServiceError mError;

	public ProcessingRequestPrioritizable(final ProcessingRequestExecutor<T> executor, final T data) {
		mExecutor = executor;
		mData = data;
	}

	@Override
	public RequestIdentifier<?> getIdentifier() {
		return mExecutor.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mExecutor.executeProcessingRequest(mData);
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