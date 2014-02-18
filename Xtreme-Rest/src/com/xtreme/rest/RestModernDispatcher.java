package com.xtreme.rest;

import com.xtreme.rest.dispatcher.ModernRequestDispatcher;

import android.app.LoaderManager;
import android.content.Context;


public class RestModernDispatcher extends ModernRequestDispatcher implements RestDispatcher {

	public RestModernDispatcher(final RestExecutor executor, final Context context, final LoaderManager manager) {
		super(executor, context, manager);
	}

	@Override
	public void setValidator(final RestQueryValidator validator) {
		final RestExecutor executor = (RestExecutor) getRequestExecutor();
		executor.setValidator(validator);
	}

}
