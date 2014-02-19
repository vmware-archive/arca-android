package com.xtreme.rest;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryListener;
import com.xtreme.rest.dispatcher.RequestDispatcher;

/**
 * This class provides a basic implementation of a single {@link RequestDispatcher} 
 * for a fragment. Using this class, fragments can request data simply by calling 
 * {@link #execute(Query)}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class RestQueryFragment extends Fragment implements QueryListener {
	
	private RestDispatcher mDispatcher;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDispatcher = onCreateRequestDispatcher(savedInstanceState);
	}
	
	protected RestDispatcher onCreateRequestDispatcher(final Bundle savedInstanceState) {
		return RestDispatcherFactory.generateRequestDispatcher(this);
	}

	protected RestDispatcher getRequestDispatcher() {
		return mDispatcher;
	}
	
	protected void execute(final Query query) {
		final RestDispatcher dispatcher = getRequestDispatcher();
		dispatcher.execute(query, this);
	}
}
