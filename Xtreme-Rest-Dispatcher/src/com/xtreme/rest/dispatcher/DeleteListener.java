package com.xtreme.rest.dispatcher;

public interface DeleteListener extends ContentRequestListener<DeleteResult> {
	@Override
	public void onRequestComplete(DeleteResult result);
}
