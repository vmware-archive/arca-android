package com.xtreme.rest.monitor;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.xtreme.rest.dispatcher.SupportRequestDispatcher;

public class RestSupportDispatcher extends SupportRequestDispatcher implements RestDispatcher {

	public RestSupportDispatcher(final RestExecutor executor, final Context context, final LoaderManager manager) {
		super(executor, context, manager);
	}

	@Override
	public void setRequestMonitor(final RequestMonitor monitor) {
		final RestExecutor executor = (RestExecutor) getRequestExecutor();
		executor.setRequestMonitor(monitor);
	}
	
	@Override
	public RequestMonitor getRequestMonitor() {
		final RestExecutor executor = (RestExecutor) getRequestExecutor();
		return executor.getRequestMonitor();
	}
}
