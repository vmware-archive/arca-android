/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.monitor;

import io.pivotal.arca.dispatcher.RequestDispatcher;

public interface ArcaDispatcher extends RequestDispatcher {
	public void setRequestMonitor(final RequestMonitor monitor);

	public RequestMonitor getRequestMonitor();
}
