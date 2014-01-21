package com.xtreme.rest.dispatcher;

public interface QueryListener extends ContentRequestListener<QueryResult> {
	@Override
	public void onRequestComplete(QueryResult result);
}
