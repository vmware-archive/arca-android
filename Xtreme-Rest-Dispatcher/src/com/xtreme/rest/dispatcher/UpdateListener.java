package com.xtreme.rest.dispatcher;

public interface UpdateListener extends RequestListener<UpdateResult> {
	@Override 
	public void onRequestComplete(UpdateResult result);
}
