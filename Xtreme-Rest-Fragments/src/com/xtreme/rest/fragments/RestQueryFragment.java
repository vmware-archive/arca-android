package com.xtreme.rest.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryListener;

/**
 * This class provides a basic implementation of a single {@link RestDispatcher} 
 * for a fragment. Using this class, fragments can request data simply by calling 
 * {@link #execute(Query)}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class RestQueryFragment extends Fragment implements QueryListener {
	
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
	
	protected RestQueryVerifier getQueryVerifier() {
		final RestDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			return dispatcher.getQueryVerifier();
		} else {
			return null;
		}
	}
	
	protected void setQueryVerifier(final RestQueryVerifier verifier) {
		final RestDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			dispatcher.setQueryVerifier(verifier);
		}
	}
	
	protected void execute(final Query query) {
		final RestDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			dispatcher.execute(query, this);
		}
	}
}
