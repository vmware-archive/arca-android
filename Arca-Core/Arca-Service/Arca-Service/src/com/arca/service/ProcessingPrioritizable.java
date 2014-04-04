package com.arca.service;

import com.arca.threading.Prioritizable;
import com.arca.threading.RequestIdentifier;
import com.arca.utils.Logger;

public class ProcessingPrioritizable<T> extends Prioritizable {

	private final ProcessingTask<T> mTask;
	private final T mData;

	private ServiceError mError;

	public ProcessingPrioritizable(final ProcessingTask<T> task, final T data) {
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
			mTask.executeProcessing(mData);
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