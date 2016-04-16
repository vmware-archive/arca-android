package io.pivotal.arca.dispatcher;

import android.content.Context;

abstract class SupportResultLoader<T> extends SupportLoader<T> {

	private T mResult;

	public SupportResultLoader(final Context context, final RequestExecutor executor, final Request<?> request) {
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
