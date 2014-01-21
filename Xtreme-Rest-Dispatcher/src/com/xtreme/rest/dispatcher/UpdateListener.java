package com.xtreme.rest.dispatcher;

public interface UpdateListener extends ContentRequestListener<UpdateResult> {
	@Override 
	public void onRequestComplete(UpdateResult result);
}
