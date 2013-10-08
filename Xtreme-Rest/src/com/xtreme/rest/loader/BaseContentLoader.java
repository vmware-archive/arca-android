package com.xtreme.rest.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

abstract class BaseContentLoader implements ContentLoader {
	
	protected static final class Extras {
		public static final String REQUEST = "request";
	}
	
	private static int sId = 0;
	protected final int ID = ++sId;
	
	private final Context mContext;
	private final ContentLoaderListener mListener;
	private final ContentErrorReceiver mReceiver;
	
	protected abstract void startLoader(final ContentRequest request);
	protected abstract void destroyLoader();
	protected abstract void cancelLoader();

	public BaseContentLoader(final Context context, final ContentLoaderListener listener) {
		mReceiver = new ContentErrorReceiver(listener);
		mListener = listener;
		mContext = context;
	}

	public Context getContext() {
		return mContext;
	}
	
	@Override
	public final void execute(final ContentRequest request) {
		final Uri uri = request.getContentUri();
		mReceiver.update(uri);
		startLoader(request);
	}
	
	@Override
	public final void cancel() {
		cancelLoader();
	}
	
	@Override
	public final void destroy() {
		mReceiver.unregister();
		destroyLoader();
	}

	protected void notifyLoadFinished(final Cursor cursor) {
		if (mListener != null) {
			final ContentResponse response = new ContentResponse(cursor);
			mListener.onLoaderFinished(response);
		}
	}
	
	protected void notifyLoaderReset() {
		if (mListener != null) {
			mListener.onLoaderReset();
		}
	}
	
	protected Bundle createRequestBundle(final ContentRequest request) {
		final Bundle bundle = new Bundle();
		bundle.putParcelable(Extras.REQUEST, request);
		return bundle;
	}
}
