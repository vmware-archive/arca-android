package com.xtreme.rest.service.test.mock;

import android.content.Context;
import android.os.Message;

import com.xtreme.rest.service.OperationHandler;
import com.xtreme.rest.service.RequestExecutor;


public class TestOperationHandler extends OperationHandler {

	public TestOperationHandler(final Context context, final RequestExecutor executor) {
		super(context, executor);
	}
	
	@Override
	public boolean sendMessageAtTime(final Message msg, final long uptimeMillis) {
		handleMessage(msg);
		return true;
	}
	
}
