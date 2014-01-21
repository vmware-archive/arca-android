package com.xtreme.rest.dispatcher;

import android.content.Context;

abstract class ModernIntegerLoader<T> extends ModernResultLoader<T> {

	private T mResult;
	
	public ModernIntegerLoader(final Context context, final RequestExecutor executor, final ContentRequest<?> request) {
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