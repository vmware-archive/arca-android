package com.xtreme.rest.dispatcher;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class SupportResultLoader<T> extends AsyncTaskLoader<T> implements ContentErrorListener {

	private final ContentErrorReceiver mReceiver = new ContentErrorReceiver(this);
	
	private final RequestExecutor mExecutor;
	private final ContentRequest<?> mRequest;
	
	public SupportResultLoader(final Context context, final RequestExecutor executor, final ContentRequest<?> request) {
		super(context);
		mReceiver.register(request.getUri());
		mExecutor = executor;
		mRequest = request;
	}
	
	public RequestExecutor getRequestExecutor() {
		return mExecutor;
	}
	
	public ContentRequest<?> getContentRequest() {
		return mRequest;
	}

	@Override
	public void reset() {
		super.reset();
		mReceiver.unregister();
	}
}