package com.arca.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.arca.dispatcher.Query;
import com.arca.dispatcher.QueryListener;
import com.arca.monitor.ArcaDispatcher;
import com.arca.monitor.RequestMonitor;

/**
 * This class provides a basic implementation of a single {@link ArcaDispatcher} 
 * for a fragment. Using this class, fragments can request data simply by calling 
 * {@link #execute(Query)}.
 */
public abstract class ArcaQuerySupportFragment extends Fragment implements QueryListener {
	
	private ArcaDispatcher mDispatcher;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDispatcher = onCreateDispatcher(savedInstanceState);
	}
	
	protected ArcaDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
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