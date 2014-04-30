/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

            if (isStarted()) {
                super.deliverResult(result);
            }

            if (result.isValid()) {
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