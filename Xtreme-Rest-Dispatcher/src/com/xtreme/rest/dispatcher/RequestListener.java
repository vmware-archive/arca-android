package com.xtreme.rest.dispatcher;

public interface RequestListener<T> {
	public void onRequestComplete(T result);
	public void onRequestReset();
}
