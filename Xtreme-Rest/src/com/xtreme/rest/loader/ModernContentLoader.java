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
class ModernContentLoader extends BaseContentLoader implements LoaderCallbacks<Cursor> {

	private final LoaderManager mLoaderManager;
	private ModernCursorLoader mLoader;

	public ModernContentLoader(final Context context, final ContentLoaderListener listener, final LoaderManager loaderManager) {
		super(context, listener);
		mLoaderManager = loaderManager;
	}

	@Override
	protected void startLoader() {
		if (mLoader == null) {
			mLoaderManager.initLoader(ID, null, this);
		} else {
			mLoader.setRequest(mRequest);
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
	public final Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
		mLoader = new ModernCursorLoader(getContext());
		mLoader.setRequest(mRequest);
		return mLoader;
	}

	@Override
	public final void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
		notifyLoadFinished(cursor);
	}

	@Override
	public final void onLoaderReset(final Loader<Cursor> loader) {
		notifyLoaderReset();
	}
}
