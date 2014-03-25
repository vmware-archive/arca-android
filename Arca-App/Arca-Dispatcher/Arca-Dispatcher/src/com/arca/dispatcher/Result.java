package com.arca.dispatcher;

public abstract class Result<T> {

	private final T mData;
	private final Error mError;
	
	private boolean mIsSyncing = false;
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
	
	public void setIsSyncing(final boolean syncing) {
		mIsSyncing = syncing;
	}
	
	public boolean isSyncing() {
		return mIsSyncing;
	}
	
	public void setIsValid(final boolean valid) {
		mIsValid = valid;
	}
	
	public boolean isValid() {
		return mIsValid && !hasError();
	}
}
