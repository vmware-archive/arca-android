package io.pivotal.arca.dispatcher;

import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.database.Cursor;

public interface RequestExecutor {

	QueryResult execute(Query request);

	UpdateResult execute(Update request);

	InsertResult execute(Insert request);

	DeleteResult execute(Delete request);

    BatchResult execute(Batch request);

	class DefaultRequestExecutor implements RequestExecutor {

		private final ContentResolver mResolver;

		public DefaultRequestExecutor(final ContentResolver resolver) {
			mResolver = resolver;
		}

		protected ContentResolver getContentResolver() {
			return mResolver;
		}

		@Override
		public QueryResult execute(final Query request) {
			final ContentResolver resolver = getContentResolver();
			final Cursor cursor = resolver.query(request.getUri(), request.getProjection(), request.getWhereClause(), request.getWhereArgs(), request.getSortOrder());
			if (cursor != null) cursor.getCount();
			return new QueryResult(cursor);
		}

		@Override
		public UpdateResult execute(final Update request) {
			final ContentResolver resolver = getContentResolver();
			final int count = resolver.update(request.getUri(), request.getContentValues(), request.getWhereClause(), request.getWhereArgs());
			return new UpdateResult(count);
		}

		@Override
		public InsertResult execute(final Insert request) {
			final ContentResolver resolver = getContentResolver();
			final int count = resolver.bulkInsert(request.getUri(), request.getContentValues());
			return new InsertResult(count);
		}

		@Override
		public DeleteResult execute(final Delete request) {
			final ContentResolver resolver = getContentResolver();
			final int count = resolver.delete(request.getUri(), request.getWhereClause(), request.getWhereArgs());
			return new DeleteResult(count);
		}

        @Override
        public BatchResult execute(final Batch request) {
            final ContentResolver resolver = getContentResolver();

            try {
                final ContentProviderResult[] results = resolver.applyBatch(request.getAuthority(), request.getOperations());
                return new BatchResult(results);
            } catch (final Exception e) {
                return new BatchResult(new Error(0, e.getMessage()));
            }
        }
    }
}
