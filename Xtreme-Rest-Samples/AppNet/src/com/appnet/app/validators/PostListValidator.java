package com.appnet.app.validators;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.appnet.app.operations.PostListOperation;
import com.xtreme.rest.loader.ContentState;
import com.xtreme.rest.providers.DatasetValidator;
import com.xtreme.rest.service.RestService;

public class PostListValidator implements DatasetValidator {

	@Override
	public ContentState validate(final Uri uri, final Cursor cursor) {
		// TODO need better validation here.
		return (cursor.getCount() > 0) ? ContentState.VALID : ContentState.INVALID; 
	}

	@Override
	public boolean fetchData(final Context context, final Uri uri, final Cursor cursor) {
		return RestService.start(context, new PostListOperation(uri));
	}
}
