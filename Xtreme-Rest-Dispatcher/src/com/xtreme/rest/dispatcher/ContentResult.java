package com.xtreme.rest.dispatcher;

public class ContentResult<T> {

	private final T mData;
	private final ContentError mError;

	public ContentResult(final T data) {
		mData = data;
		mError = null;
	}
	
	public ContentResult(final ContentError error) {
		mData = null;
		mError = error;
	}

	public T getResult() {
		return mData;
	}
	
	public ContentError getError() {
		return mError;
	}
	
	public boolean hasError() {
		return mError != null;
	}
}
