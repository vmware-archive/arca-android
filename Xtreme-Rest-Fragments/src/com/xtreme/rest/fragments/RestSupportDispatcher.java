package com.xtreme.rest.fragments;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.xtreme.rest.dispatcher.SupportRequestDispatcher;


public class RestSupportDispatcher extends SupportRequestDispatcher implements RestDispatcher {

	public RestSupportDispatcher(final RestExecutor executor, final Context context, final LoaderManager manager) {
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
