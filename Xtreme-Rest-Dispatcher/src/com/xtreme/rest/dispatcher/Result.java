package com.xtreme.rest.dispatcher;

public abstract class Result<T> {

	private final T mData;
	private final Error mError;
	
	private boolean mIsRefreshing = false;
	private boolean mIsValid = true;

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
	
	public void setIsRefreshing(final boolean refreshing) {
		mIsRefreshing = refreshing;
	}
	
	public boolean isRefreshing() {
		return mIsRefreshing;
	}
	
	public void setIsValid(final boolean valid) {
		mIsValid = valid;
	}
	
	public boolean isValid() {
		return mIsValid;
	}
}
