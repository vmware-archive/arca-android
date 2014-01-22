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
		mReceiver.register(request.getUri());
		mExecutor = executor;
		mRequest = request;
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
		final T result = getResult();
		if (result != null) {
			deliverResult(result);
		}
		if (takeContentChanged() || result == null) {
			forceLoad();
		}
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