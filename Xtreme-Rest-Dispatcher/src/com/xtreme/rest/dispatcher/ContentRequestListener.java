package com.xtreme.rest.dispatcher;

public interface ContentRequestListener<T> {
	public void onRequestComplete(T result);
	public void onRequestReset();
}
