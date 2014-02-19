package com.xtreme.rest;

import com.xtreme.rest.dispatcher.RequestDispatcher;


public interface RestDispatcher extends RequestDispatcher {
	public void setValidator(final RestQueryValidator validator);
	public RestQueryValidator getValidator();
}
