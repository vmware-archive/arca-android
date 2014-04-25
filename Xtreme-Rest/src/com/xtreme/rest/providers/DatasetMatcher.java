package com.xtreme.rest.providers;

import java.util.Collection;

import android.content.UriMatcher;
import android.net.Uri;

import com.xtreme.rest.utils.ClassUtils;

class DatasetMatcher {

	private int MATCH = 0;
	private final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private final RestMapping<Dataset> mDatasetMapping = new RestMapping<Dataset>();
	private final RestMapping<DatasetValidator> mValidatorMapping = new RestMapping<DatasetValidator>();

	public void register(final String authority, final String path, final Class<? extends Dataset> datasetClass, final Class<? extends DatasetValidator> validatorClass) {
		final int match = MATCH++;
		mUriMatcher.addURI(authority, path, match);
		mDatasetMapping.append(match, (Dataset) ClassUtils.getInstanceFromClass(datasetClass));
		mValidatorMapping.append(match, (DatasetValidator) ClassUtils.getInstanceFromClass(validatorClass));
	}

	public Dataset getDataset(final Uri uri) {
		final int match = mUriMatcher.match(uri);
		final Dataset dataset = mDatasetMapping.get(match);
		return dataset;
	}
	
	public DatasetValidator getValidator(final Uri uri) {
		final int match = mUriMatcher.match(uri);
		final DatasetValidator validator = mValidatorMapping.get(match);
		return validator;
	}
	
	public Collection<Dataset> getDatasets() {
		return mDatasetMapping.collect();
	}

}
