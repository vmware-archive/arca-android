package com.xtreme.rest.dispatcher;

import android.content.Context;

public class SupportQueryLoader extends SupportLoader<QueryResult> {
	
	private final ForceLoadContentObserver mObserver;
	private final QueryResultTracker mTracker;

	public SupportQueryLoader(final Context context, final RequestExecutor executor, final Query request) {
		super(context, executor, request);
		mObserver = new ForceLoadContentObserver();
		mTracker = new QueryResultTracker();
	}

	@Override
	public QueryResult loadInBackground() {
		final Query request = (Query) getContentRequest();
		final RequestExecutor executor = getRequestExecutor();
		return executor.execute(request);
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