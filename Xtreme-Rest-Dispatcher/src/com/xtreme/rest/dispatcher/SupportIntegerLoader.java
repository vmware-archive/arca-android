package com.xtreme.rest.dispatcher;

import android.content.Context;

abstract class SupportIntegerLoader<T> extends SupportResultLoader<T> {

	private T mResult;
	
	public SupportIntegerLoader(final Context context, final RequestExecutor executor, final ContentRequest<?> request) {
		super(context, executor, request);
	}

	@Override
	public void deliverResult(final T result) {
		if (isReset()) {
			return;
		}
		
		mResult = result;

		if (isStarted()) {
			super.deliverResult(result);
		}
	}

	@Override
	public void clearResult() {
		mResult = null;
	}
	
	@Override
	protected T getResult() {
		return mResult;
	}
}