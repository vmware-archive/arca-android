package com.arca.dispatcher;

public interface RequestListener<T> {
	public void onRequestComplete(T result);
	public void onRequestReset();
}
