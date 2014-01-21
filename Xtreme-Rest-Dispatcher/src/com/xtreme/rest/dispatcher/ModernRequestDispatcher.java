package com.xtreme.rest.dispatcher;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernRequestDispatcher extends AbstractRequestLoaderDispatcher {

	private final Context mContext;
	private final LoaderManager mLoaderManager;

	public ModernRequestDispatcher(final RequestExecutor executor, final Context context, final LoaderManager manager) {
		super(executor);
		mContext = context;
		mLoaderManager = manager;
	}

	@Override
	protected void dispatch(final ContentRequest<?> request, final ContentRequestListener<?> listener) {
		final int identifier = request.getIdentifier();
		final Bundle bundle = createRequestBundle(request);
		final LoaderCallbacks<?> callbacks = createCallbacks(request, listener);
		mLoaderManager.restartLoader(identifier, bundle, callbacks);
	}

	@SuppressWarnings("unchecked")
	private LoaderCallbacks<?> createCallbacks(final ContentRequest<?> request, final ContentRequestListener<?> listener) {
		if (request instanceof Query) {
			return new CursorLoaderCallbacks((ContentRequestListener<Cursor>) listener);
		} else {
			return new IntegerLoaderCallbacks((ContentRequestListener<Integer>) listener);
		}
	}

	private class CursorLoaderCallbacks extends NotififierCallbacks<Cursor, CursorResult> {
		
		public CursorLoaderCallbacks(final ContentRequestListener<Cursor> listener) {
			super(listener);
		}

		@Override
		public Loader<CursorResult> onCreateLoader(final int id, final Bundle args) {
			final RequestExecutor executor = getRequestExecutor();
			final Query request = args.getParcelable(Extras.REQUEST);
			return new ModernCursorLoader(mContext, executor, request);
		}
	}
	
	private class IntegerLoaderCallbacks extends NotififierCallbacks<Integer, ContentResult<Integer>> {
		
		public IntegerLoaderCallbacks(final ContentRequestListener<Integer> listener) {
			super(listener);
		}

		@Override
		public Loader<ContentResult<Integer>> onCreateLoader(final int id, final Bundle args) {
			final RequestExecutor executor = getRequestExecutor();
			final ContentRequest<?> request = args.getParcelable(Extras.REQUEST);
			return new ModernIntegerLoader(mContext, executor, request);
		}
	}
	
	private abstract class NotififierCallbacks<DATA_TYPE, RESULT_TYPE extends ContentResult<DATA_TYPE>> implements LoaderCallbacks<RESULT_TYPE> {
		
		protected final ContentRequestListener<DATA_TYPE> mListener;

		public NotififierCallbacks(final ContentRequestListener<DATA_TYPE> listener) {
			mListener = listener;
		}

		@Override
		public void onLoadFinished(final Loader<RESULT_TYPE> loader, final RESULT_TYPE result) {
			mListener.onRequestComplete(result);
		}

		@Override
		public void onLoaderReset(final Loader<RESULT_TYPE> loader) {
			mListener.onRequestComplete(null);
		}
	}
}
