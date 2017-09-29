package io.pivotal.arca.monitor;

import io.pivotal.arca.dispatcher.RequestDispatcher;

public interface ArcaDispatcher extends RequestDispatcher {
	void setRequestMonitor(final RequestMonitor monitor);

	RequestMonitor getRequestMonitor();
}
