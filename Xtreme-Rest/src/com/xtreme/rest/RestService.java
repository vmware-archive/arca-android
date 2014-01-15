package com.xtreme.rest;

import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.ValidatorMatcher.DefaultValidatorMatcher;
import com.xtreme.rest.service.OperationService;

public class RestService extends OperationService {

	private static final ValidatorMatcher MATCHER = new DefaultValidatorMatcher();

	public static void register(final String authority, final String path, final Class<? extends Validator> klass) {
		MATCHER.register(authority, path, klass);
	}

	public ValidatorState validateQuery(final Uri uri, final Cursor cursor) {
		final Validator validator = MATCHER.matchValidator(uri);
		final ValidatorState state = validator.validate(uri, cursor);
		return state;
	} 
	
	public class RestBinder extends ServiceBinder {
        @Override
		public RestService getService() {
            return RestService.this;
        }
    }
}
