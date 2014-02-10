package com.xtreme.rest.dispatcher;

public interface RequestDispatcher extends RequestExecutor {
	
	public void execute(Query request, QueryListener listener);

	public void execute(Update request, UpdateListener listener);

	public void execute(Insert request, InsertListener listener);

	public void execute(Delete request, DeleteListener listener);
}
