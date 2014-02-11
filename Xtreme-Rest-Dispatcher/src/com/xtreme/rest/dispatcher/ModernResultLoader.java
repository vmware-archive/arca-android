package com.xtreme.rest.dispatcher;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
abstract class ModernResultLoader<T> extends ModernLoader<T> {

	private T mResult;
	
	public ModernResultLoader(final Context context, final RequestExecutor executor, final Request<?> request) {
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