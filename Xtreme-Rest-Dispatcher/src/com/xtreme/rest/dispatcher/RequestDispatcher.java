package com.xtreme.rest.dispatcher;

import android.os.Bundle;

public interface RequestDispatcher extends RequestExecutor {
	
	public void execute(Query request, QueryListener listener);

	public void execute(Update request, UpdateListener listener);

	public void execute(Insert request, InsertListener listener);

	public void execute(Delete request, DeleteListener listener);
	
	
	public static abstract class AbstractRequestDispatcher implements RequestDispatcher {
		
		protected static interface Extras {
			public static final String REQUEST = "request";
		}
		
		private final RequestExecutor mRequestExecutor;

		public AbstractRequestDispatcher(final RequestExecutor executor) {
			mRequestExecutor = executor;
		}
		
		public RequestExecutor getRequestExecutor() {
			return mRequestExecutor;
		}
		
		@Override
		public QueryResult execute(final Query request) {
			return getRequestExecutor().execute(request);
		}
		
		@Override
		public UpdateResult execute(final Update request) {
			return getRequestExecutor().execute(request);
		}
		
		@Override
		public InsertResult execute(final Insert request) {
			return getRequestExecutor().execute(request);
		}
		
		@Override
		public DeleteResult execute(final Delete request) {
			return getRequestExecutor().execute(request);
		}
		
		protected static Bundle createRequestBundle(final Request<?> request) {
			final Bundle bundle = new Bundle();
			bundle.putParcelable(Extras.REQUEST, request);
			return bundle;
		}
	}
}
