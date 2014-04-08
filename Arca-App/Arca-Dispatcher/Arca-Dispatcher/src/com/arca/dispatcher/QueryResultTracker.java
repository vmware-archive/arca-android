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
package com.arca.dispatcher;

import java.util.HashSet;
import java.util.Set;

import android.database.ContentObserver;

final class QueryResultTracker {

	private final Set<QueryResult> mRegistered = new HashSet<QueryResult>();

	private QueryResult mInvalidResult;
	private QueryResult mValidResult;

	public QueryResult getResult() {
		return mValidResult;
	}

	public void registerObserver(final QueryResult result, final ContentObserver observer) {
		if (result != null && !mRegistered.contains(result)) {
			result.registerContentObserver(observer);
			mRegistered.add(result);
		}
	}

	public void trackValidResult(final QueryResult result) {
		if (mInvalidResult != result) {
			stopTracking(mInvalidResult);
			mInvalidResult = null;
		}

		if (mValidResult != result) {
			stopTracking(mValidResult);
		}

		mValidResult = result;
	}

	public void trackInvalidResult(final QueryResult result, final ContentObserver observer) {
		if (mValidResult != null && mValidResult != result && mRegistered.contains(mValidResult)) {
			mValidResult.unregisterContentObserver(observer);
			mRegistered.remove(mValidResult);
		}

		if (mInvalidResult != result) {
			stopTracking(mInvalidResult);
		}

		mInvalidResult = result;
	}

	public void stopTracking(final QueryResult result) {
		if (result != null && !result.isClosed()) {
			result.close();
			mRegistered.remove(result);
		}
	}

	public void reset() {
		stopTracking(mValidResult);
		stopTracking(mInvalidResult);

		mValidResult = null;
		mInvalidResult = null;
	}
}