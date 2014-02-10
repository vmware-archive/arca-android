package com.xtreme.rest.dispatcher;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
abstract class ModernLoader<T> extends AsyncTaskLoader<T> implements ErrorListener {

	private final ErrorReceiver mReceiver;
	private final RequestExecutor mExecutor;
	private final Request<?> mRequest;
	
	public ModernLoader(final Context context, final RequestExecutor executor, final Request<?> request) {
		super(context);
		mReceiver = new ErrorReceiver(this);
		mExecutor = executor;
		mRequest = request;
		register(request);
	}

	private void register(final Request<?> request) {
		if (request != null) {
			mReceiver.register(request.getUri());
		}
	}
	
	@Override
	public abstract T loadInBackground();
	
	protected abstract T getErrorResult(final Error error);
	
	protected abstract T getResult();

	protected abstract void clearResult();
	
	
	public RequestExecutor getRequestExecutor() {
		return mExecutor;
	}
	
	public Request<?> getContentRequest() {
		return mRequest;
	}
	
	@Override
	protected void onStartLoading() {
		forceLoad();
	}
	
	@Override
	protected void onStopLoading() {
		cancelLoad();
	}
	
	@Override
	public void onRequestError(final Error error) {
		final T result = getErrorResult(error);
		deliverResult(result);
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		clearResult();
	}

	@Override
	public void reset() {
		super.reset();
		mReceiver.unregister();
	}
}