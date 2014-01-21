package com.xtreme.rest.dispatcher;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

abstract class SupportLoader<T> extends AsyncTaskLoader<T> implements ContentErrorListener {

	private final ContentErrorReceiver mReceiver;
	private final RequestExecutor mExecutor;
	private final ContentRequest<?> mRequest;
	
	public SupportLoader(final Context context, final RequestExecutor executor, final ContentRequest<?> request) {
		super(context);
		mReceiver = new ContentErrorReceiver(this);
		mReceiver.register(request.getUri());
		mExecutor = executor;
		mRequest = request;
	}
	
	@Override
	public abstract T loadInBackground();
	
	protected abstract T getErrorResult(final ContentError error);
	
	protected abstract T getResult();

	protected abstract void clearResult();
	
	
	public RequestExecutor getRequestExecutor() {
		return mExecutor;
	}
	
	public ContentRequest<?> getContentRequest() {
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
	public void onRequestError(final ContentError error) {
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