package com.xtreme.rest;

import java.util.Collection;

import android.content.UriMatcher;
import android.net.Uri;

public interface ValidatorMatcher {
	
	/**
	 * Register a {@link Validator} for a given authority and path. 
	 * 
	 * The result can be retrieved by calling {@link ValidatorMatcher#matchValidator(Uri)} with 
	 * a {@link Uri} that matches the authority and path provided.
	 * 
	 * @param authority
	 * @param path
	 * @param datasetClass
	 */
	public void register(final String authority, final String path, final Class<? extends Validator> validatorClass);
	
	/**
	 * Matches the given {@link Uri} to a {@link Validator}.
	 * 
	 * @param uri
	 * @return the {@link Validator} matching the given uri
	 */
	public Validator matchValidator(final Uri uri);
	
	/**
	 * Gives access to the entire collection of {@link Validator}s that have been registered.
	 * 
	 * @return the collection of {@link Validator}s
	 */
	public Collection<Validator> getValidators();

	
	public static class DefaultValidatorMatcher implements ValidatorMatcher {

		private int MATCH = 0;
		private final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		private final RestMapping<Validator> mValidatorMapping = new RestMapping<Validator>();

		@Override
		public void register(final String authority, final String path, final Class<? extends Validator> klass) {
			final int match = MATCH++;
			mUriMatcher.addURI(authority, path, match);
			mValidatorMapping.append(match, klass);
		}

		@Override
		public Validator matchValidator(final Uri uri) {
			final int match = mUriMatcher.match(uri);
			final Validator validator = mValidatorMapping.get(match);
			return validator;
		}

		@Override
		public Collection<Validator> getValidators() {
			return mValidatorMapping.collect();
		}
	}
}
