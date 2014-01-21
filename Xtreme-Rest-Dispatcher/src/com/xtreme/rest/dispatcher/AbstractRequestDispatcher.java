package com.xtreme.rest.dispatcher;

import android.database.Cursor;

abstract class AbstractRequestDispatcher implements RequestDispatcher {
	
	protected abstract void dispatch(ContentRequest<?> request, ContentRequestListener<?> listener);

	private final RequestExecutor mRequestExecutor;

	public AbstractRequestDispatcher(final RequestExecutor executor) {
		mRequestExecutor = executor;
	}
	
	public RequestExecutor getRequestExecutor() {
		return mRequestExecutor;
	}

	@Override
	public void execute(final Query request, final ContentRequestListener<Cursor> listener) {
		dispatch(request, listener);
	}
	
	@Override
	public void execute(final Update request, final ContentRequestListener<Integer> listener) {
		dispatch(request, listener);
	}
	
	@Override
	public void execute(final Insert request, final ContentRequestListener<Integer> listener) {
		dispatch(request, listener);
	}
	
	@Override
	public void execute(final Delete request, final ContentRequestListener<Integer> listener) {
		dispatch(request, listener);
	}
	
	@Override
	public ContentResult<Cursor> execute(final Query request) {
		return getRequestExecutor().execute(request);
	}
	
	@Override
	public ContentResult<Integer> execute(final Update request) {
		return getRequestExecutor().execute(request);
	}
	
	@Override
	public ContentResult<Integer> execute(final Insert request) {
		return getRequestExecutor().execute(request);
	}
	
	@Override
	public ContentResult<Integer> execute(final Delete request) {
		return getRequestExecutor().execute(request);
	}
}
