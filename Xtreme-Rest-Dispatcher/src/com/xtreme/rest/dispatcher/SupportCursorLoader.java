package com.xtreme.rest.dispatcher;

import android.content.Context;

public class SupportCursorLoader extends SupportResultLoader<CursorResult> {
	
	private final ForceLoadContentObserver mObserver;
	private final CursorResultTracker mTracker;

	public SupportCursorLoader(final Context context, final RequestExecutor executor, final Query request) {
		super(context, executor, request);
		mObserver = new ForceLoadContentObserver();
		mTracker = new CursorResultTracker();
	}

	@Override
	public Query getContentRequest() {
		return (Query) super.getContentRequest();
	}

	@Override
	public CursorResult loadInBackground() {
		final Query request = getContentRequest();
		return (CursorResult) getRequestExecutor().execute(request);
	}

	@Override
	protected void onStartLoading() {
		final CursorResult result = mTracker.getCursorResult();
		if (result != null) {
			deliverResult(result);
		}
		if (takeContentChanged() || result == null) {
			forceLoad();
		}
	}

	@Override
	public void deliverResult(final CursorResult result) {
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
	public void onRequestError(final ContentError error) {
		deliverResult(new CursorResult(error));
	}

	@Override
	public void onCanceled(final CursorResult result) {
		mTracker.stopTracking(result);
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		mTracker.reset();
	}

}