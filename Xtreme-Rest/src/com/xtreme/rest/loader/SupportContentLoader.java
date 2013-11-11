package com.xtreme.rest.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class SupportContentLoader extends BaseContentLoader implements LoaderCallbacks<Cursor> {

	private final LoaderManager mLoaderManager;
	private SupportCursorLoader mLoader;

	public SupportContentLoader(final Context context, final ContentLoaderListener listener, final LoaderManager loaderManager) {
		super(context, listener);
		mLoaderManager = loaderManager;
	}
	
	private void initLoader(final ContentRequest request) {
		final Bundle bundle = createRequestBundle(request);
		mLoaderManager.initLoader(ID, bundle, this);
	}
	
	@Override
	protected void startLoader(final ContentRequest request) {
		if (mLoader == null) {
			initLoader(request);
		} else {
			mLoader.setRequest(request);
			mLoader.forceLoad();
		}
	}

	@Override
	protected void cancelLoader() {
		if (mLoader != null) {
			mLoader.cancelLoad();
		}
	}
	
	@Override
	protected void destroyLoader() {
		mLoaderManager.destroyLoader(ID);
	}

	@Override
	public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
		final ContentRequest request = args.getParcelable(Extras.REQUEST);
		mLoader = new SupportCursorLoader(getContext());
		mLoader.setRequest(request);
		return mLoader;
	}
	
	@Override
	public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
		notifyLoadFinished(cursor);
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
		notifyLoaderReset();
	}
}
