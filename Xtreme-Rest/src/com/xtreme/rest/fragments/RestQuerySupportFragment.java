package com.xtreme.rest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xtreme.rest.RestDispatcher;
import com.xtreme.rest.RestDispatcherFactory;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryListener;
import com.xtreme.rest.dispatcher.RequestDispatcher;

/**
 * This class provides a basic implementation of a single {@link RequestDispatcher} 
 * for a fragment. Using this class, fragments can request data simply by calling 
 * {@link #execute(Query)}.
 */
public abstract class RestQuerySupportFragment extends Fragment implements QueryListener {
	
	private RestDispatcher mDispatcher;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDispatcher = onCreateRequestDispatcher();
	}
	
	protected RestDispatcher onCreateRequestDispatcher() {
		return RestDispatcherFactory.generateRequestDispatcher(this);
	}
	
	/**
	 * @return The {@link RequestDispatcher} instance used by this fragment.
	 */
	protected RestDispatcher getRequestDispatcher() {
		return mDispatcher;
	}
	
	/**
	 * @see {@link RequestDispatcher#execute(Query, QueryListener)}
	 */
	protected void execute(final Query query) {
		final RestDispatcher dispatcher = getRequestDispatcher();
		dispatcher.execute(query, this);
	}
}
