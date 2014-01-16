package com.rottentomatoes.app.validators;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.loader.ContentState;
import com.xtreme.rest.loader.Validator;

public class MovieValidator implements Validator {
	
	@Override
	public ContentState validate(final Uri uri, final Cursor cursor) {
		return (cursor.getCount() > 0) ? ContentState.VALID : ContentState.INVALID; 
	}

	@Override
	public boolean fetchData(final Context context, final Uri uri, final Cursor cursor) {
		// return RestService.start(context, new MovieOperation(uri));
		return false;
	}
}
