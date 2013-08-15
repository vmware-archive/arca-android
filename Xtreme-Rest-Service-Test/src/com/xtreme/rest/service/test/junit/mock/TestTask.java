package com.xtreme.rest.service.test.junit.mock;

import android.content.Context;

import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public class TestTask extends Task<String> {

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		return new RequestIdentifier<String>("test_task");
	}
	
	@Override
	public String onExecuteNetworkRequest(final Context context) throws Exception {
		return null;
	}

	@Override
	public void onExecuteProcessingRequest(final Context context, final String data) throws Exception {
		
	}
}
