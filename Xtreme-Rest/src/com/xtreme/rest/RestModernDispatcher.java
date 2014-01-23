package com.xtreme.rest;

import android.app.LoaderManager;
import android.content.Context;

import com.xtreme.rest.dispatcher.ModernRequestDispatcher;
import com.xtreme.rest.validator.QueryValidator;

public class RestModernDispatcher extends ModernRequestDispatcher implements RestDispatcher {

	public RestModernDispatcher(final RestExecutor executor, final Context context, final LoaderManager manager) {
		super(executor, context, manager);
	}

	@Override
	public void addValidator(final QueryValidator validator) {
		final RestExecutor executor = (RestExecutor) getRequestExecutor();
		executor.addValidator(validator);
	}

}
