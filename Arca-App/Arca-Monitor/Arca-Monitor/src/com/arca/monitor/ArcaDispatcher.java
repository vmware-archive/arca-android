package com.arca.monitor;

import com.arca.dispatcher.RequestDispatcher;

public interface ArcaDispatcher extends RequestDispatcher {
	public void setRequestMonitor(final RequestMonitor monitor);
	public RequestMonitor getRequestMonitor();
}
