package io.pivotal.arca.dispatcher;

public interface ErrorListener {
	public void onRequestError(Error error);
}
