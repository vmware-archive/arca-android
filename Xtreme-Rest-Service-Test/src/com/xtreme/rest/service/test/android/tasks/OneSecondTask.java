package com.xtreme.rest.service.test.android.tasks;

import java.util.Locale;

import android.content.Context;
import android.util.Log;

import com.xtreme.rest.service.Task;
import com.xtreme.rest.service.test.android.activities.TestActivity;
import com.xtreme.threading.RequestIdentifier;

public final class OneSecondTask extends Task<String> {

	public static final double FAIL_PROBABILITY = 0.05;

	public static boolean ALWAYS_FAIL = false;
	public static boolean RANDOM_FAIL = false;

	private int mId;

	public OneSecondTask(int id) {
		mId = id;
	}

	@Override
	public String onExecuteNetworkRequest(Context context) throws Exception {
		Log.d(TestActivity.TAG, "onExecuteNetworkRequest for task: " + this);
		sleep();
		return String.valueOf(mId);
	}

	@Override
	public void onExecuteProcessingRequest(Context context, String data) throws Exception {
		Log.d(TestActivity.TAG, "onExecuteProcessingRequest for task: " + this);
		sleep();
	}

	private final void sleep() throws Exception {
		if (ALWAYS_FAIL || (RANDOM_FAIL && Math.random() < FAIL_PROBABILITY)) {
			Log.e(TestActivity.TAG, "Task " + this + " just failed.");
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
