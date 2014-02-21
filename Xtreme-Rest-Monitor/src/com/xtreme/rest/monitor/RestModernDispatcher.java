package com.xtreme.rest.monitor;

import android.app.LoaderManager;
import android.content.Context;

import com.xtreme.rest.dispatcher.ModernRequestDispatcher;

public class RestModernDispatcher extends ModernRequestDispatcher implements RestDispatcher {

	public RestModernDispatcher(final RestExecutor executor, final Context context, final LoaderManager manager) {
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
