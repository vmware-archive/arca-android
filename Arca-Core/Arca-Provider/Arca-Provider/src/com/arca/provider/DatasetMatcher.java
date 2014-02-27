package com.arca.provider;

import java.util.Collection;

import android.content.UriMatcher;
import android.net.Uri;

public interface DatasetMatcher {
	
	public void register(final String authority, final String path, final Class<? extends Dataset> datasetClass);
	
	public Dataset matchDataset(final Uri uri);
	
	public Collection<Dataset> getDatasets();
	
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
