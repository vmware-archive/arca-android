package com.xtreme.rest;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.xtreme.rest.dispatcher.SupportRequestDispatcher;
import com.xtreme.rest.validator.QueryValidator;

public class RestSupportDispatcher extends SupportRequestDispatcher implements RestDispatcher {

	public RestSupportDispatcher(final RestExecutor executor, final Context context, final LoaderManager manager) {
		super(executor, context, manager);
	}

	@Override
	public void addValidator(final QueryValidator validator) {
		final RestExecutor executor = (RestExecutor) getRequestExecutor();
		executor.addValidator(validator);
	}

}
