package com.xtreme.rest.service.test.junit.mock;

import android.content.Context;

import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public class TestTask extends Task<String> {

	private RequestIdentifier<?> mIdentifier;
	private Exception mNetworkException;
	private Exception mProcessingException;
	private String mNetworkResult;

	public TestTask(final RequestIdentifier<?> identifier) {
		this(identifier, null, null, null);
	}
	
	public TestTask(final RequestIdentifier<?> identifier, final String networkResult) {
		this(identifier, networkResult, null, null);
	}
	
	public TestTask(final RequestIdentifier<?> identifier, final String networkResult, final Exception networkException, final Exception processingException) {
		mIdentifier = identifier;
		mNetworkResult = networkResult;
		mNetworkException = networkException;
		mProcessingException = processingException;
	}
	
	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		return mIdentifier;
	}
	
	@Override
	public String onExecuteNetworkRequest(final Context context) throws Exception {
		
		if (mNetworkException != null) {
			throw mNetworkException;
		}
		
		return mNetworkResult;
	}

	@Override
	public void onExecuteProcessingRequest(final Context context, final String data) throws Exception {
		if (mProcessingException != null) {
			throw mProcessingException;
		}
	}
}
