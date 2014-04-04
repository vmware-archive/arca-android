package com.arca.service.test.mock;

import android.content.Context;

import com.arca.service.Task;
import com.arca.threading.RequestIdentifier;

public class TestTask extends Task<String> {

	public static interface Messages extends Task.Messages {}
	
	private final RequestIdentifier<?> mIdentifier;
	private final Exception mNetworkingException;
	private final Exception mProcessingException;
	private final String mNetworkResult;
	private Task<?> mDependency;

	public TestTask(final RequestIdentifier<?> identifier) {
		this(identifier, null, null, null);
	}
	
	public TestTask(final RequestIdentifier<?> identifier, final Task<?> task) {
		this(identifier, null, null, null);
		mDependency = task;
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
		
		if (mDependency != null) {
			addDependency(mDependency);
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
