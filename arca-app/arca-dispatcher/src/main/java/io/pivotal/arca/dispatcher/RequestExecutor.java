/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.dispatcher;

import android.content.ContentResolver;
import android.database.Cursor;

public interface RequestExecutor {

	public QueryResult execute(Query request);

	public UpdateResult execute(Update request);

	public InsertResult execute(Insert request);

	public DeleteResult execute(Delete request);

	public static class DefaultRequestExecutor implements RequestExecutor {

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
			if (cursor != null)
				cursor.getCount();
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

	}
}
