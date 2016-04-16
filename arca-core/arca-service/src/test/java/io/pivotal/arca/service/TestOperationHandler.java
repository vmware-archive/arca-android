package io.pivotal.arca.service;

import android.content.Context;
import android.os.Message;

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
