package io.pivotal.arca.monitor;

import android.app.LoaderManager;
import android.content.Context;

import io.pivotal.arca.dispatcher.ModernRequestDispatcher;

public class ArcaModernDispatcher extends ModernRequestDispatcher implements ArcaDispatcher {

	public ArcaModernDispatcher(final ArcaExecutor executor, final Context context, final LoaderManager manager) {
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
