package com.crunchbase.app.validators;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.providers.DatasetValidator;
import com.xtreme.rest.loader.ContentState;

public class CompanyValidator implements DatasetValidator {
	
	@Override
	public ContentState validate(final Uri uri, final Cursor cursor) {
		return ContentState.VALID; 
	}

	@Override
	public boolean fetchData(final Context context, final Uri uri, final Cursor cursor) {
		return false;
	}
}
