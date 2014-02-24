package com.arca.dispatcher;

public interface InsertListener extends RequestListener<InsertResult> {
	@Override
	public void onRequestComplete(InsertResult result);
}
