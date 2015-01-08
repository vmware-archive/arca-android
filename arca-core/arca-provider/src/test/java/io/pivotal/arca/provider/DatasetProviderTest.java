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
package io.pivotal.arca.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import io.pivotal.arca.provider.DatasetProviderTest.TestDatasetProvider;
import io.pivotal.arca.provider.DatasetProviderTest.TestDatasetProvider.Uris;

import junit.framework.Assert;

import java.util.Locale;

public class DatasetProviderTest extends ProviderTestCase2<TestDatasetProvider> {

	private static final Uri URI = Uri.parse("content://empty");
	private static final MatrixCursor CURSOR = new MatrixCursor(new String[] {});

	public DatasetProviderTest() {
		super(TestDatasetProvider.class, TestDatasetProvider.AUTHORITY);
	}

	public void testDatasetRetrievalSucceeds() {
		final TestDatasetProvider provider = getProvider();
		final Dataset dataset = provider.getDatasetOrThrowException(Uris.URI_1);
		assertNotNull(dataset);
	}

	public void testDatasetRetrievalThrowsException() {
		try {
			final Uri uri = Uri.parse("content://empty");
			getProvider().getDatasetOrThrowException(uri);
			Assert.fail();
		} catch (final Exception e) {
			assertNotNull(e);
		}
	}

	public void testDatasetProviderType() {
		final TestDatasetProvider provider = getProvider();
		final String type = provider.getType(Uris.URI_1);
		final String name = provider.getDatasetOrThrowException(Uris.URI_1).getClass().getName();
		final String expected = String.format("vnd.android.cursor.dir/%s", name);
		assertEquals(expected.toLowerCase(Locale.getDefault()), type);
	}

	public void testDatasetProviderQuery() {
		final TestDatasetProvider provider = getProvider();
		final Cursor cursor = provider.query(Uris.URI_1, null, null, null, null);
		assertEquals(CURSOR, cursor);
	}

	public void testDatasetProviderUpdate() {
		final TestDatasetProvider provider = getProvider();
		final int updated = provider.update(Uris.URI_1, null, null, null);
		assertEquals(10, updated);
	}

	public void testDatasetProviderInsert() {
		final TestDatasetProvider provider = getProvider();
		final Uri uri = provider.insert(Uris.URI_1, null);
		assertEquals(URI, uri);
	}

	public void testDatasetProviderBulkInsert() {
		final TestDatasetProvider provider = getProvider();
		final int inserted = provider.bulkInsert(Uris.URI_1, null);
		assertEquals(10, inserted);
	}

	public void testDatasetProviderDelete() {
		final TestDatasetProvider provider = getProvider();
		final int deleted = provider.delete(Uris.URI_1, null, null);
		assertEquals(10, deleted);
	}

	// ==================================

	public static class TestDatasetProvider extends DatasetProvider {

		public static final String AUTHORITY = "com.test.authority";

		private static final class Paths {
			public static final String PATH_1 = "path1";
		}

		public static final class Uris {
			public static final Uri URI_1 = Uri.parse("content://" + AUTHORITY + "/" + Paths.PATH_1);
		}

		@Override
		public boolean onCreate() {
			registerDataset(AUTHORITY, Paths.PATH_1, TestTable1.class);
			return true;
		}

		@Override
		public Dataset getDatasetOrThrowException(final Uri uri) {
			return super.getDatasetOrThrowException(uri);
		}
	}

	public static class TestTable1 implements Dataset {

		@Override
		public Uri insert(final Uri uri, final ContentValues values) {
			return URI;
		}

		@Override
		public int bulkInsert(final Uri uri, final ContentValues[] values) {
			return 10;
		}

		@Override
		public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
			return 10;
		}

		@Override
		public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
			return 10;
		}

		@Override
		public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
			return CURSOR;
		}
	}
}
