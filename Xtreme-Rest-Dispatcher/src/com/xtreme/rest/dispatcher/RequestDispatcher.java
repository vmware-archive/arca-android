package com.xtreme.rest.dispatcher;

public interface RequestDispatcher extends RequestExecutor {
	
	public void execute(Query request, QueryListener listener);

	public void execute(Update request, UpdateListener listener);

	public void execute(Insert request, InsertListener listener);

	public void execute(Delete request, DeleteListener listener);

//	public <T> void execute(ContentRequest<T> request, ContentRequestListener<T> listener);
//	
//	public <T> ContentResult<T> execute(ContentRequest<T> request);
}
