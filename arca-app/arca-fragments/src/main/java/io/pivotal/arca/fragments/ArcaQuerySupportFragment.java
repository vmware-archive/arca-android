/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.pivotal.arca.dispatcher.Query;
import io.pivotal.arca.dispatcher.QueryListener;
import io.pivotal.arca.monitor.ArcaDispatcher;
import io.pivotal.arca.monitor.RequestMonitor;

public abstract class ArcaQuerySupportFragment extends Fragment implements QueryListener {

	private ArcaDispatcher mDispatcher;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDispatcher = onCreateDispatcher(savedInstanceState);
	}

	public ArcaDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
		return ArcaDispatcherFactory.generateDispatcher(this);
	}

	protected ArcaDispatcher getRequestDispatcher() {
		return mDispatcher;
	}

	protected RequestMonitor getRequestMonitor() {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			return dispatcher.getRequestMonitor();
		} else {
			return null;
		}
	}

	protected void setRequestMonitor(final RequestMonitor monitor) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			dispatcher.setRequestMonitor(monitor);
		}
	}

	protected void execute(final Query query) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			dispatcher.execute(query, this);
		}
	}
}