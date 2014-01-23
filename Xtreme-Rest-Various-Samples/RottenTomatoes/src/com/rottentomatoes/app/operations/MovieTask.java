package com.rottentomatoes.app.operations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.rottentomatoes.app.datasets.MovieTable;
import com.rottentomatoes.app.models.Movie;
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;
import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public class MovieTask extends Task<String> {

	private final String mId;
	
	public MovieTask(final String id) {
		mId = id;
	}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		// TODO this needs to be a unique identifier across all tasks
		return new RequestIdentifier<String>(String.format("movie::%s", mId));
	}
	
	@Override
	public String onExecuteNetworkRequest(final Context context) throws Exception {
		// TODO make network request to fetch object(s) 
		// return RottenTomatoesRequests.getMovie(mId);
		throw new Exception("Override this method to return a json string for a Movie.");
	}

	@Override
	public void onExecuteProcessingRequest(final Context context, final String data) throws Exception {
		// TODO parse the response and insert into content provider
		final Movie item = new Gson().fromJson(data, Movie.class);
		final ContentValues values = MovieTable.getContentValues(item);
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(RottenTomatoesContentProvider.Uris.MOVIES_URI, values);
	}
}
