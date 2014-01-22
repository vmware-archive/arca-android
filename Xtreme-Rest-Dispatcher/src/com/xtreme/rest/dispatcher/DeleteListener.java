package com.xtreme.rest.dispatcher;

public interface DeleteListener extends RequestListener<DeleteResult> {
	@Override
	public void onRequestComplete(DeleteResult result);
}
