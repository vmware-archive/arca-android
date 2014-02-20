package com.xtreme.rest.fragments;

import android.app.LoaderManager;
import android.content.Context;

import com.xtreme.rest.dispatcher.ModernRequestDispatcher;


public class RestModernDispatcher extends ModernRequestDispatcher implements RestDispatcher {

	public RestModernDispatcher(final RestExecutor executor, final Context context, final LoaderManager manager) {
		super(executor, context, manager);
	}

	@Override
	public void setQueryVerifier(final RestQueryVerifier validator) {
		final RestExecutor executor = (RestExecutor) getRequestExecutor();
		executor.setQueryVerifier(validator);
	}
	
	@Override
	public RestQueryVerifier getQueryVerifier() {
		final RestExecutor executor = (RestExecutor) getRequestExecutor();
		return executor.getQueryVerifier();
	}

}
