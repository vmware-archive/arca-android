package io.pivotal.arca.dispatcher;

import android.database.ContentObserver;

import java.util.HashSet;
import java.util.Set;

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
