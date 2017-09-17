package io.pivotal.arca.dispatcher;

import android.os.Bundle;

public interface RequestDispatcher extends RequestExecutor {

	void execute(Query request, QueryListener listener);

	void execute(Update request, UpdateListener listener);

	void execute(Insert request, InsertListener listener);

	void execute(Delete request, DeleteListener listener);

	void execute(Batch request, BatchListener listener);


	abstract class AbstractRequestDispatcher implements RequestDispatcher {

		interface Extras {
			String REQUEST = "request";
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

        @Override
        public BatchResult execute(final Batch request) {
            return getRequestExecutor().execute(request);
        }

		protected static Bundle createRequestBundle(final Request<?> request) {
			final Bundle bundle = new Bundle();
			bundle.putParcelable(Extras.REQUEST, request);
			return bundle;
		}
	}
}
