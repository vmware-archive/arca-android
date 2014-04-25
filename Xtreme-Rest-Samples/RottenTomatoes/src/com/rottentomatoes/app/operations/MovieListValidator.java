package com.rottentomatoes.app.operations;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.rottentomatoes.app.providers.RottenTomatoesUriCache;
import com.xtreme.rest.loader.ContentState;
import com.xtreme.rest.providers.DatasetValidator;
import com.xtreme.rest.service.RestService;

public class MovieListValidator implements DatasetValidator {
	
	@Override
	public ContentState validate(final Uri uri, final Cursor cursor) {
		if (cursor.getCount() == 0) return ContentState.INVALID; 
		final boolean expired = RottenTomatoesUriCache.isExpired(uri);
		return expired ? ContentState.EXPIRED : ContentState.VALID; 
	}

	@Override
	public boolean fetchData(final Context context, final Uri uri, final Cursor cursor) {
		return RestService.start(context, new MovieListOperation(uri));
	}
}
