package com.xtreme.rest.dispatcher;

import java.util.HashSet;
import java.util.Set;

import android.database.ContentObserver;

final class CursorResultTracker {

	private final Set<CursorResult> mRegistered = new HashSet<CursorResult>();

	private CursorResult mInvalidResult;
	private CursorResult mValidResult;

	public CursorResult getCursorResult() {
		return mValidResult;
	}

	public void registerObserver(final CursorResult result, final ContentObserver observer) {
		if (result != null && !mRegistered.contains(result)) {
			result.registerContentObserver(observer);
			mRegistered.add(result);
		}
	}

	public void trackValidResult(final CursorResult result) {
		if (mInvalidResult != result) {
			stopTracking(mInvalidResult);
			mInvalidResult = null;
		}

		if (mValidResult != result) {
			stopTracking(mValidResult);
		}

		mValidResult = result;
	}

	public void trackInvalidResult(final CursorResult result, final ContentObserver observer) {
		if (mValidResult != null && mValidResult != result && mRegistered.contains(mValidResult)) {
			mValidResult.unregisterContentObserver(observer);
			mRegistered.remove(mValidResult);
		}

		if (mInvalidResult != result) {
			stopTracking(mInvalidResult);
		}

		mInvalidResult = result;
	}

	public void stopTracking(final CursorResult result) {
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