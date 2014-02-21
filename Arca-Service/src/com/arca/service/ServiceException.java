package com.arca.service;

/**
 * An {@link Exception} that wraps a {@link ServiceError}.
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = -922340197478254879L;

	private final ServiceError mError;

	public ServiceException(final ServiceError error) {
		mError = error;
	}

	public ServiceError getError() {
		return mError;
	}

}
