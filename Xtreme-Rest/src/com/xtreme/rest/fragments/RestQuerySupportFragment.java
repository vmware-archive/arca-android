package com.xtreme.rest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryListener;
import com.xtreme.rest.dispatcher.RequestDispatcher;
import com.xtreme.rest.dispatcher.RequestDispatcherFactory;

/**
 * This class provides a basic implementation of a single {@link RequestDispatcher} 
 * for a fragment. Using this class, fragments can request data simply by calling 
 * {@link #execute(Query)}.
 */
public abstract class RestQuerySupportFragment extends Fragment implements QueryListener {
	
	private RequestDispatcher mDispatcher;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDispatcher = RequestDispatcherFactory.generateRequestDispatcher(this);
	}
	
	/**
	 * @return The {@link RequestDispatcher} instance used by this fragment.
	 */
	protected final RequestDispatcher getRequestDispatcher() {
		return mDispatcher;
	}
	
	/**
	 * @see {@link RequestDispatcher#execute(Query, QueryListener)}
	 */
	protected final void execute(final Query query) {
		getRequestDispatcher().execute(query, this);
	}
}
