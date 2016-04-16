package io.pivotal.arca.dispatcher;

public interface RequestListener<T> {
	public void onRequestComplete(T result);
}
