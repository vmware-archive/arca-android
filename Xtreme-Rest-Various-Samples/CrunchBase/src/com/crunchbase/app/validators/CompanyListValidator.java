package com.crunchbase.app.validators;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.crunchbase.app.operations.CompanyListOperation;
import com.xtreme.rest.RestService;
import com.xtreme.rest.loader.ContentState;
import com.xtreme.rest.loader.Validator;

public class CompanyListValidator implements Validator {

	@Override
	public ContentState validate(final Uri uri, final Cursor cursor) {
		return (cursor.getCount() > 0) ? ContentState.VALID : ContentState.INVALID; 
	}

	@Override
	public boolean fetchData(final Context context, final Uri uri, final Cursor cursor) {
		return RestService.start(context, new CompanyListOperation(uri));
	}
}
