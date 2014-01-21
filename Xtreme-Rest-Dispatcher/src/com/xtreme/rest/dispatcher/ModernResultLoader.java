package com.xtreme.rest.dispatcher;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class ModernResultLoader<T> extends AsyncTaskLoader<T> implements ContentErrorListener {

	private final ContentErrorReceiver mReceiver = new ContentErrorReceiver(this);
	
	private final RequestExecutor mExecutor;
	private final ContentRequest<?> mRequest;
	
	public ModernResultLoader(final Context context, final RequestExecutor executor, final ContentRequest<?> request) {
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