package com.xtreme.rest.fragments;

import com.xtreme.rest.dispatcher.RequestDispatcher;

public interface RestDispatcher extends RequestDispatcher {
	public void setQueryVerifier(final RestQueryVerifier verifier);
	public RestQueryVerifier getQueryVerifier();
}
