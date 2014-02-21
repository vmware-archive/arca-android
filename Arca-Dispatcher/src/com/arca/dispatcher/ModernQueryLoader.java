package com.arca.dispatcher;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernQueryLoader extends ModernLoader<QueryResult> {

	private final ForceLoadContentObserver mObserver;
	private final QueryResultTracker mTracker;

	public ModernQueryLoader(final Context context, final RequestExecutor executor, final Query request) {
		super(context, executor, request);
		mObserver = new ForceLoadContentObserver();
		mTracker = new QueryResultTracker();
	}

	@Override
	protected void onStartLoading() {
		final QueryResult result = getResult();
		if (result != null) {
			deliverResult(result);
		}
		if (takeContentChanged() || result == null) {
			forceLoad();
		}
	}
	
	@Override
	public QueryResult loadInBackground() {
		final Query request = (Query) getContentRequest();
		final RequestExecutor executor = getRequestExecutor();
		final QueryResult result = executor.execute(request);
		final ContentResolver resolver = getContext().getContentResolver();
		result.setNotificationUri(resolver, request.getUri());
		return result;
	}

	@Override
	public void deliverResult(final QueryResult result) {
		mTracker.registerObserver(result, mObserver);

		if (isReset()) {
			mTracker.reset();

		} else if (result.isValid()) {
			if (isStarted()) {
				super.deliverResult(result);
			}
			mTracker.trackValidResult(result);
		} else {
			mTracker.trackInvalidResult(result, mObserver);
		}
	}

	@Override
	protected QueryResult getResult() {
		return mTracker.getResult();
	}
	
	@Override
	public QueryResult getErrorResult(final Error error) {
		return new QueryResult(error);
	}
	
	@Override
	public void onCanceled(final QueryResult result) {
		mTracker.stopTracking(result);
	}

	@Override
	public void clearResult() {
		mTracker.reset();
	}
}