package com.rottentomatoes.app.operations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.arca.provider.DataUtils;
import com.arca.service.Task;
import com.google.gson.Gson;
import com.rottentomatoes.app.models.Movie;
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;
import com.xtreme.threading.RequestIdentifier;

public class MovieTask extends Task<String> {

	private final String mId;
	
	public MovieTask(final String id) {
		mId = id;
	}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		return new RequestIdentifier<String>(String.format("movie::%s", mId));
	}
	
	@Override
	public String onExecuteNetworking(final Context context) throws Exception {
		throw new Exception("Override this method to return a json string for a Movie.");
	}

	@Override
	public void onExecuteProcessing(final Context context, final String data) throws Exception {
		final Movie item = new Gson().fromJson(data, Movie.class);
		final ContentValues values = DataUtils.getContentValues(item);
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(RottenTomatoesContentProvider.Uris.MOVIES_URI, values);
	}
}
