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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import io.pivotal.arca.provider.DatasetMatcher.DefaultMatcher;

import java.util.Collection;
import java.util.Locale;

public abstract class DatasetProvider extends ContentProvider {

	private final DatasetMatcher mMatcher = new DefaultMatcher();

	protected final Collection<Dataset> getDatasets() {
		return mMatcher.getDatasets();
	}

	protected final void registerDataset(final String authority, final String path, final Class<? extends Dataset> datasetClass) {
		mMatcher.register(authority, path, datasetClass);
	}

	// ======================================================

	@Override
	public String getType(final Uri uri) {
		final String name = getDatasetOrThrowException(uri).getClass().getName();
		final String type = String.format("vnd.android.cursor.dir/%s", name);
		return type.toLowerCase(Locale.getDefault());
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final Uri contentUri = dataset.insert(uri, values);
		return contentUri;
	}

	@Override
	public int bulkInsert(final Uri uri, final ContentValues[] values) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final int numRowsInserted = dataset.bulkInsert(uri, values);
		return numRowsInserted;
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final int numRowsAffected = dataset.update(uri, values, selection, selectionArgs);
		return numRowsAffected;
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final int numRowsDeleted = dataset.delete(uri, selection, selectionArgs);
		return numRowsDeleted;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final Cursor cursor = dataset.query(uri, projection, selection, selectionArgs, sortOrder);
		return cursor;
	}

	// ======================================================

	protected Dataset getDatasetOrThrowException(final Uri uri) {
		final Dataset dataset = mMatcher.matchDataset(uri);
		if (dataset == null) {
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return dataset;
	}

}
