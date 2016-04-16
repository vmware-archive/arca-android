package io.pivotal.arca.service;

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
