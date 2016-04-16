package io.pivotal.arca.dispatcher;

public interface QueryListener extends RequestListener<QueryResult> {
	@Override
	public void onRequestComplete(QueryResult result);
}
