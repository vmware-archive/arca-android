package com.arca.provider;

import java.util.Collection;

import android.content.UriMatcher;
import android.net.Uri;

public interface DatasetMatcher {
	
	/**
	 * Register a {@link Dataset} for a given authority and path. 
	 * 
	 * The result can be retrieved by calling {@link DatasetMatcher#matchDataset(Uri)} with 
	 * a {@link Uri} that matches the authority and path provided.
	 * 
	 * @param authority
	 * @param path
	 * @param datasetClass
	 */
	public void register(final String authority, final String path, final Class<? extends Dataset> datasetClass);
	
	/**
	 * Matches the given {@link Uri} to a {@link Dataset}.
	 * 
	 * @param uri
	 * @return the {@link Dataset} matching the given uri
	 */
	public Dataset matchDataset(final Uri uri);
	
	/**
	 * Gives access to the entire collection of {@link Dataset}s that have been registered.
	 * 
	 * @return the collection of {@link Dataset}s
	 */
	public Collection<Dataset> getDatasets();
	
	/**
	 * This class uses a {@link UriMatcher} to map {@link Uri}s to {@link Dataset}s
	 */
	public static class DefaultMatcher implements DatasetMatcher {

		private int MATCH = 0;
		private final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		private final ClassMapping<Dataset> mDatasetMapping = new ClassMapping<Dataset>();

		@Override
		public void register(final String authority, final String path, final Class<? extends Dataset> klass) {
			final int match = MATCH++;
			mUriMatcher.addURI(authority, path, match);
			mDatasetMapping.append(match, klass);
		}

		@Override
		public Dataset matchDataset(final Uri uri) {
			final int match = mUriMatcher.match(uri);
			final Dataset dataset = mDatasetMapping.get(match);
			return dataset;
		}
		
		@Override
		public Collection<Dataset> getDatasets() {
			return mDatasetMapping.values();
		}
	}
}
