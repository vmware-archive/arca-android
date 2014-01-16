package com.crunchbase.app.validators;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.loader.ContentState;
import com.xtreme.rest.loader.Validator;

public class CompanyValidator implements Validator {
	
	@Override
	public ContentState validate(final Uri uri, final Cursor cursor) {
		return ContentState.VALID; 
	}

	@Override
	public boolean fetchData(final Context context, final Uri uri, final Cursor cursor) {
		return false;
	}
}
