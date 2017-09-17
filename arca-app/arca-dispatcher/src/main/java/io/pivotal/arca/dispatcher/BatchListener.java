package io.pivotal.arca.dispatcher;

public interface BatchListener extends RequestListener<BatchResult> {
	@Override
	public void onRequestComplete(BatchResult result);
}
