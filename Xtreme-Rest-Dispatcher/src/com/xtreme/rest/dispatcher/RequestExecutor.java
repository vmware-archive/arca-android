package com.xtreme.rest.dispatcher;

import android.content.ContentResolver;
import android.database.Cursor;

public interface RequestExecutor {

	public ContentResult<Cursor> execute(Query request);

	public ContentResult<Integer> execute(Update request);

	public ContentResult<Integer> execute(Insert request);

	public ContentResult<Integer> execute(Delete request);
	
	
	public static class DefaultRequestExecutor implements RequestExecutor {

		private final ContentResolver mResolver;

		public DefaultRequestExecutor(final ContentResolver resolver) {
			mResolver = resolver;
		}
		
		protected ContentResolver getContentResolver() {
			return mResolver;
		}
		
		@Override
		public ContentResult<Cursor> execute(final Query request) {
			final ContentResolver resolver = getContentResolver();
			final Cursor cursor = resolver.query(request.getUri(), request.getProjection(), request.getWhereClause(), request.getWhereArgs(), request.getSortOrder());
			if (cursor != null) {
				cursor.getCount();
			}
			return new CursorResult(cursor);
		}

		@Override
		public ContentResult<Integer> execute(final Update request) {
			final ContentResolver resolver = getContentResolver();
			final int count = resolver.update(request.getUri(), request.getContentValues(), request.getWhereClause(), request.getWhereArgs());
			return new ContentResult<Integer>(count);
		}

		@Override
		public ContentResult<Integer> execute(final Insert request) {
			final ContentResolver resolver = getContentResolver();
			final int count = resolver.bulkInsert(request.getUri(), request.getContentValues());
			return new ContentResult<Integer>(count);
		}

		@Override
		public ContentResult<Integer> execute(final Delete request) {
			final ContentResolver resolver = getContentResolver();
			final int count = resolver.delete(request.getUri(), request.getWhereClause(), request.getWhereArgs());
			return new ContentResult<Integer>(count);
		}
		
	}
}
