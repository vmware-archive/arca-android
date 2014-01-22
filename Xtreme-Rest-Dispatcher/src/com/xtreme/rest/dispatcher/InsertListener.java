package com.xtreme.rest.dispatcher;

public interface InsertListener extends RequestListener<InsertResult> {
	@Override
	public void onRequestComplete(InsertResult result);
}
