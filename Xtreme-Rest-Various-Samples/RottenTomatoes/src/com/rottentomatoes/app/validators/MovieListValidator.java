package com.rottentomatoes.app.validators;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.service.Validator;
import com.xtreme.rest.service.ValidatorState;

public class MovieListValidator implements Validator {

	@Override
	public boolean validate(final Uri uri, final Cursor cursor) {
		return (cursor.getCount() > 0) ? ContentState.VALID : ContentState.INVALID; 
	}

	@Override
	public boolean fetchData(final Context context, final Uri uri, final Cursor cursor) {
		// return RestService.start(context, new MovieListOperation(uri));
		return false;
	}
}
