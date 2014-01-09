package com.xtreme.rest.providers;

import java.util.Collection;
import java.util.Locale;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.loader.ContentLoader;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.providers.DatasetMatcher.DefaultMatcher;

/**
 * The base {@link ContentProvider} class to be used with {@link Dataset}s, 
 * {@link ContentLoader}s, and {@link ContentRequest}s. 
 * 
 * This class initializes a {@link DatasetMatcher} that acts as a router 
 * between a {@link Uri} and a {@link Dataset}.
 */
public abstract class DatasetProvider extends ContentProvider {

	private DatasetMatcher mMatcher = new DefaultMatcher();
	
	/**
	 * Provides access to the collection of {@link Dataset}s that have been registered 
	 * with this provider.
	 * 
	 * @return A collection of {@link Dataset}s.
	 */
	protected Collection<Dataset> getDatasets() {
		return mMatcher.getDatasets();
	}
	
	/**
	 * Register a {@link Dataset} with this {@link ContentProvider} on the specified path.
	 * 
	 * @param authority The {@link ContentProvider}'s authority
	 * @param path The path associated with this {@link Dataset}
	 * @param datasetClass The {@link Dataset}'s class
	 */
	protected void registerDataset(final String authority, final String path, final Class<? extends Dataset> datasetClass) {
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
	
	private Dataset getDatasetOrThrowException(final Uri uri) {
		final Dataset dataset = mMatcher.matchDataset(uri);
		if (dataset == null) {
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return dataset;
	}
}
