package com.xtreme.rest.service;

import android.os.Handler;
import android.os.Message;

public class OperationHandler extends Handler implements OperationObserver {

	private static final int NOTIFY_COMPLETE = 100;
	
	private final RestService mService;
	
	public OperationHandler(final RestService service) {
		mService = service;
	}

	@Override
	public void onOperationComplete(final Operation operation) {
		final Message message = obtainMessage(NOTIFY_COMPLETE, operation);
		sendMessage(message);
	}
	
	@Override
	public void handleMessage(final Message msg) {
		if (msg.what == NOTIFY_COMPLETE) {
			final Operation operation = (Operation) msg.obj;
			mService.handleFinish(operation);
		}
	}
}