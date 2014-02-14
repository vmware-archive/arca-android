package com.xtreme.rest.service.test.mock;

import android.content.Context;

import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public class TestTask extends Task<String> {

	public static interface Messages extends Task.Messages {}
	
	private RequestIdentifier<?> mIdentifier;
	private Exception mNetworkingException;
	private Exception mProcessingException;
	private String mNetworkResult;

	public TestTask(final RequestIdentifier<?> identifier) {
		this(identifier, null, null, null);
	}
	
	public TestTask(final RequestIdentifier<?> identifier, final String networkResult) {
		this(identifier, networkResult, null, null);
	}
	
	public TestTask(final RequestIdentifier<?> identifier, final String networkResult, final Exception networkingException, final Exception processingException) {
		mIdentifier = identifier;
		mNetworkResult = networkResult;
		mNetworkingException = networkingException;
		mProcessingException = processingException;
	}
	
	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		return mIdentifier;
	}
	
	@Override
	public String onExecuteNetworking(final Context context) throws Exception {
		
		if (mNetworkingException != null) {
			throw mNetworkingException;
		}
		
		return mNetworkResult;
	}

	@Override
	public void onExecuteProcessing(final Context context, final String data) throws Exception {
		if (mProcessingException != null) {
			throw mProcessingException;
		}
	}
}
