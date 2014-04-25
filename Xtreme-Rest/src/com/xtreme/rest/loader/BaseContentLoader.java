package com.xtreme.rest.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

abstract class BaseContentLoader implements ContentLoader {
	
	private static int sId = 0;
	protected final int ID = ++sId;
	
	private final Context mContext;
	private final ContentLoaderListener mListener;
	private final ContentErrorReceiver mReceiver;
	
	protected ContentRequest mRequest;

	protected abstract void startLoader();
	protected abstract void destroyLoader();
	protected abstract void cancelLoader();

	public BaseContentLoader(final Context context, final ContentLoaderListener listener) {
		mReceiver = new ContentErrorReceiver(listener);
		mListener = listener;
		mContext = context;
	}

	@Override
	public synchronized final void execute(final ContentRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("ContentRequest must not be null.");
		}

		final Uri uri = request.getContentUri();
		
		mReceiver.update(uri);
		mRequest = request;
		
		startLoader();
	}
	
	@Override
	public synchronized final void cancel() {
		cancelLoader();
	}
	
	public final Context getContext() {
		return mContext;
	}

	void notifyLoadFinished(final Cursor cursor) {
		if (mListener != null) {
			final ContentResponse response = new ContentResponse(cursor);
			mListener.onLoaderFinished(response);
		}
	}
	
	void notifyLoaderReset() {
		if (mListener != null) {
			mListener.onLoaderReset();
		}
	}
	
	@Override
	public final void destroy() {
		mReceiver.unregister();
		destroyLoader();
	}
}
