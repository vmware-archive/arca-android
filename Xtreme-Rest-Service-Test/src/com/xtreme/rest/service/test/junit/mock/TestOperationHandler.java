package com.xtreme.rest.service.test.junit.mock;

import android.os.Message;

import com.xtreme.rest.service.OperationHandler;
import com.xtreme.rest.service.RestService;


public class TestOperationHandler extends OperationHandler {

	public TestOperationHandler(final RestService service) {
		super(service);
	}
	
	@Override
	public boolean sendMessageAtTime(final Message msg, final long uptimeMillis) {
		handleMessage(msg);
		return true;
	}
	
}
