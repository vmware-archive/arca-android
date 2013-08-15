package com.xtreme.rest.providers;

import java.util.Collection;

import com.xtreme.rest.utils.ClassUtils;

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
	 * Register a {@link Dataset} and {@link DatasetValidator} for a given authority and path. 
	 * 
	 * The {@link Dataset} portion can be retrieved by calling {@link DatasetMatcher#matchDataset(Uri)} while 
	 * the {@link DatasetValidator} portion can be retrieve by calling {@link DatasetMatcher#matchValidator(Uri)}
	 * both by supplying a {@link Uri} that matches the authority and path provided.
	 * 
	 * @param authority
	 * @param path
	 * @param datasetClass
	 * @param validatorClass
	 */
	public void register(final String authority, final String path, final Class<? extends Dataset> datasetClass, final Class<? extends DatasetValidator> validatorClass);
	
	/**
	 * Matches the given {@link Uri} to a {@link Dataset}.
	 * 
	 * @param uri
	 * @return the {@link Dataset} matching the given uri
	 */
	public Dataset matchDataset(final Uri uri);
	
	/**
	 * Matches the given {@link Uri} to a {@link DatasetValidator}.
	 * 
	 * @param uri
	 * @return the {@link DatasetValidator} matching the given uri
	 */
	public DatasetValidator matchValidator(final Uri uri);
	
	/**
	 * Gives access to the entire collection of {@link Dataset}s that have been registered.
	 * 
	 * @return the collection of {@link Dataset}s
	 */
	public Collection<Dataset> getDatasets();
	
	
	/**
	 * Gives access to the entire collection of {@link DatasetValidator}s that have been registered.
	 * 
	 * @return the collection of {@link DatasetValidator}s
	 */
	public Collection<DatasetValidator> getValidators();
	
	/**
	 * This class uses a {@link UriMatcher} to map {@link Uri}s to {@link Dataset}s
	 */
	public static class DefaultMatcher implements DatasetMatcher {

		private int MATCH = 0;
		private final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		private final RestMapping<Dataset> mDatasetMapping = new RestMapping<Dataset>();
		private final RestMapping<DatasetValidator> mValidatorMapping = new RestMapping<DatasetValidator>();

		@Override
		public void register(String authority, String path, Class<? extends Dataset> datasetClass) {
			register(authority, path, datasetClass, null);
		}
		
		@Override
		public void register(final String authority, final String path, final Class<? extends Dataset> datasetClass, final Class<? extends DatasetValidator> validatorClass) {
			final int match = MATCH++;
			mUriMatcher.addURI(authority, path, match);
			mDatasetMapping.append(match, (Dataset) ClassUtils.getInstanceFromClass(datasetClass));
			mValidatorMapping.append(match, (DatasetValidator) ClassUtils.getInstanceFromClass(validatorClass));
		}

		@Override
		public Dataset matchDataset(final Uri uri) {
			final int match = mUriMatcher.match(uri);
			final Dataset dataset = mDatasetMapping.get(match);
			return dataset;
		}
		
		@Override
		public DatasetValidator matchValidator(final Uri uri) {
			final int match = mUriMatcher.match(uri);
			final DatasetValidator validator = mValidatorMapping.get(match);
			return validator;
		}
		
		@Override
		public Collection<Dataset> getDatasets() {
			return mDatasetMapping.collect();
		}
		
		@Override
		public Collection<DatasetValidator> getValidators() {
			return mValidatorMapping.collect();
		}
		
	}
}
