package io.pivotal.arca.dispatcher;

public interface DeleteListener extends RequestListener<DeleteResult> {
	@Override
	public void onRequestComplete(DeleteResult result);
}
