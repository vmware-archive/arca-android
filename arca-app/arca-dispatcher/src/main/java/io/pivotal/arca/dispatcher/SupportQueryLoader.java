/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

import android.content.ContentResolver;
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

        } else {

            if (isStarted() && result.isValid()) {
                super.deliverResult(result);
            }

            if (result.isValid() && !result.hasError()) {
                mTracker.trackValidResult(result);
            } else {
                mTracker.trackInvalidResult(result, mObserver);
            }
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