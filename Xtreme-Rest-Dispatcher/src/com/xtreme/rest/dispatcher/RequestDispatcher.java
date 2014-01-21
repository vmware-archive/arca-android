package com.xtreme.rest.dispatcher;

import android.database.Cursor;

public interface RequestDispatcher extends RequestExecutor {
	
	public void execute(Query request, ContentRequestListener<Cursor> listener);

	public void execute(Update request, ContentRequestListener<Integer> listener);

	public void execute(Insert request, ContentRequestListener<Integer> listener);

	public void execute(Delete request, ContentRequestListener<Integer> listener);

//	public <T> void execute(ContentRequest<T> request, ContentRequestListener<T> listener);
//	
//	public <T> ContentResult<T> execute(ContentRequest<T> request);
}
