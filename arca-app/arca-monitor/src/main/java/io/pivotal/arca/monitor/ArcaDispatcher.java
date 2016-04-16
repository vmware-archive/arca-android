package io.pivotal.arca.monitor;

import io.pivotal.arca.dispatcher.RequestDispatcher;

public interface ArcaDispatcher extends RequestDispatcher {
	public void setRequestMonitor(final RequestMonitor monitor);

	public RequestMonitor getRequestMonitor();
}
