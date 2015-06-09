/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.monitor;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import io.pivotal.arca.dispatcher.SupportRequestDispatcher;

public class ArcaSupportDispatcher extends SupportRequestDispatcher implements ArcaDispatcher {

	public ArcaSupportDispatcher(final ArcaExecutor executor, final Context context, final LoaderManager manager) {
		super(executor, context, manager);
	}

	@Override
	public void setRequestMonitor(final RequestMonitor monitor) {
		final ArcaExecutor executor = (ArcaExecutor) getRequestExecutor();
		executor.setRequestMonitor(monitor);
	}

	@Override
	public RequestMonitor getRequestMonitor() {
		final ArcaExecutor executor = (ArcaExecutor) getRequestExecutor();
		return executor.getRequestMonitor();
	}
}
