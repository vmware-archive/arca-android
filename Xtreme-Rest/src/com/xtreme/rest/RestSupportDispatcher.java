package com.xtreme.rest;

import com.xtreme.rest.dispatcher.SupportRequestDispatcher;

import android.content.Context;
import android.support.v4.app.LoaderManager;


public class RestSupportDispatcher extends SupportRequestDispatcher implements RestDispatcher {

	public RestSupportDispatcher(final RestExecutor executor, final Context context, final LoaderManager manager) {
		super(executor, context, manager);
	}

	@Override
	public void setValidator(final RestQueryValidator validator) {
		final RestExecutor executor = (RestExecutor) getRequestExecutor();
		executor.setValidator(validator);
	}

}
