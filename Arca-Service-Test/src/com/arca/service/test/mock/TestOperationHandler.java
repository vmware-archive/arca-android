package com.arca.service.test.mock;

import android.content.Context;
import android.os.Message;

import com.arca.service.OperationHandler;
import com.arca.service.RequestExecutor;


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
