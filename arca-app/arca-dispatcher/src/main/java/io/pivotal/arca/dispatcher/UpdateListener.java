package io.pivotal.arca.dispatcher;

public interface UpdateListener extends RequestListener<UpdateResult> {
	@Override
	public void onRequestComplete(UpdateResult result);
}
