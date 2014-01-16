package com.rottentomatoes.app.validators;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.Validator;
import com.xtreme.rest.ValidatorState;

public class MovieValidator implements Validator {
	
	@Override
	public ValidatorState validate(final Uri uri, final Cursor cursor) {
		return (cursor.getCount() > 0) ? ValidatorState.VALID : ValidatorState.INVALID; 
	}

	@Override
	public boolean fetchData(final Context context, final Uri uri, final Cursor cursor) {
		// return RestService.start(context, new MovieOperation(uri));
		return false;
	}
}
