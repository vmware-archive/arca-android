package com.xtreme.rest.loader;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernContentLoader extends BaseContentLoader implements LoaderCallbacks<Cursor> {

	private final LoaderManager mLoaderManager;
	private ModernCursorLoader mLoader;

	public ModernContentLoader(final Context context, final ContentLoaderListener listener, final LoaderManager loaderManager) {
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
		mLoader = new ModernCursorLoader(getContext());
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
