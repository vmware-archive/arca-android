package com.xtreme.rest.dispatcher;

abstract class Result<T> {

	private final T mData;
	private final Error mError;

	public Result(final T data) {
		mData = data;
		mError = null;
	}
	
	public Result(final Error error) {
		mData = null;
		mError = error;
	}

	public T getResult() {
		return mData;
	}
	
	public Error getError() {
		return mError;
	}
	
	public boolean hasError() {
		return mError != null;
	}
}
