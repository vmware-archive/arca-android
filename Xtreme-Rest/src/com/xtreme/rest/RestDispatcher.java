package com.xtreme.rest;

import com.xtreme.rest.dispatcher.RequestDispatcher;
import com.xtreme.rest.validator.QueryValidator;

public interface RestDispatcher extends RequestDispatcher {
	public void addValidator(final QueryValidator validator);
}
