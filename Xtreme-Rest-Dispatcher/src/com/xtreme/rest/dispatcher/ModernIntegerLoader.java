package com.xtreme.rest.dispatcher;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernIntegerLoader extends ModernResultLoader<ContentResult<Integer>> {

	private ContentResult<Integer> mResult;

	public ModernIntegerLoader(final Context context, final RequestExecutor executor, final ContentRequest<?> request) {
		super(context, executor, request);
	}

	@Override
	public ContentResult<Integer> loadInBackground() {
		final ContentRequest<?> request = getContentRequest();
		
		if (request instanceof Update) {
			return getRequestExecutor().execute((Update) request);
			
		} else if (request instanceof Insert) {
			return getRequestExecutor().execute((Insert) request);
			
		} else if (request instanceof Delete) {
			return getRequestExecutor().execute((Delete) request);
			
		} else {
			return null;
		}
	}

	@Override
	protected void onStartLoading() {
		if (mResult != null) {
			deliverResult(mResult);
		}
		if (takeContentChanged() || mResult == null) {
			forceLoad();
		}
	}

	@Override
	public void deliverResult(final ContentResult<Integer> result) {
		if (isReset()) {
			return;
		}
		
		mResult = result;

		if (isStarted()) {
			super.deliverResult(result);
		}
	}

	@Override
	public void onRequestError(final ContentError error) {
		mResult = new ContentResult<Integer>(error);
		
		deliverResult(mResult);
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		mResult = null;
	}
}