package com.xtreme.rest.dispatcher;

public interface InsertListener extends ContentRequestListener<InsertResult> {
	@Override
	public void onRequestComplete(InsertResult result);
}
