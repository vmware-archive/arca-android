package com.xtreme.rest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryListener;

/**
 * This class provides a basic implementation of a single {@link RestDispatcher} 
 * for a fragment. Using this class, fragments can request data simply by calling 
 * {@link #execute(Query)}.
 */
public abstract class RestQuerySupportFragment extends Fragment implements QueryListener {
	
	private RestDispatcher mDispatcher;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDispatcher = onCreateDispatcher(savedInstanceState);
	}
	
	protected RestDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
		return RestDispatcherFactory.generateDispatcher(this);
	}
	
	protected RestDispatcher getRequestDispatcher() {
		return mDispatcher;
	}
	
	protected void execute(final Query query) {
		final RestDispatcher dispatcher = getRequestDispatcher();
		dispatcher.execute(query, this);
	}
}