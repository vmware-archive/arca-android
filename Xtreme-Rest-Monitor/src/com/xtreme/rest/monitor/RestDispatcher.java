package com.xtreme.rest.monitor;

import com.xtreme.rest.dispatcher.RequestDispatcher;

public interface RestDispatcher extends RequestDispatcher {
	public void setRequestMonitor(final RequestMonitor monitor);
	public RequestMonitor getRequestMonitor();
}
