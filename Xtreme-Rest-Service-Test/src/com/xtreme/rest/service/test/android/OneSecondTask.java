package com.xtreme.rest.service.test.android;

import java.util.Locale;

import android.content.Context;
import android.util.Log;

import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public final class OneSecondTask extends Task<String> {

	static boolean ALWAYS_FAIL = false;
	static boolean RANDOM_FAIL = false;
	private static final double FAIL_PROBABILITY = 0.05;

	private int mId;

	public OneSecondTask(int id) {
		mId = id;
	}

	@Override
	public String onExecuteNetworkRequest(Context context) throws Exception {
		Log.d(Tests.TAG, "onExecuteNetworkRequest for task: " + this);
		sleep();
		return String.valueOf(mId);
	}

	@Override
	public void onExecuteProcessingRequest(Context context, String data) throws Exception {
		Log.d(Tests.TAG, "onExecuteProcessingRequest for task: " + this);
		sleep();
	}

	private final void sleep() throws Exception {
		if (ALWAYS_FAIL || (RANDOM_FAIL && Math.random() < FAIL_PROBABILITY)) {
			Log.e(Tests.TAG, "Task " + this + " just failed.");
			throw new RuntimeException();
		}
		Thread.sleep(1000);
	}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		return new RequestIdentifier<String>(this.toString());
	}

	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "T%d", mId);
	}

}
