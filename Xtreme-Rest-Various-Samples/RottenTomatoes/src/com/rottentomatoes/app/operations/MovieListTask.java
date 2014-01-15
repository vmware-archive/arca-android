package com.rottentomatoes.app.operations;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.rottentomatoes.app.application.RottenTomatoesRequests;
import com.rottentomatoes.app.datasets.MovieTable;
import com.rottentomatoes.app.datasets.MovieTypeTable;
import com.rottentomatoes.app.models.Movie;
import com.rottentomatoes.app.models.MoviesResponse;
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;
import com.rottentomatoes.app.providers.RottenTomatoesUriCache;
import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public class MovieListTask extends Task<List<Movie>> {

	private final String mType;
	
	public MovieListTask(final String type) {
		mType = type;
	}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		return new RequestIdentifier<String>("movie_list:" + mType);
	}
	
	@Override
	public List<Movie> onExecuteNetworkRequest(final Context context) throws Exception {
		final MoviesResponse response = RottenTomatoesRequests.getMovieList(mType, 30, "ca");
		return response.getMovies();
	}

	@Override
	public void onExecuteProcessingRequest(final Context context, final List<Movie> data) throws Exception {
		final ContentResolver resolver = context.getContentResolver();
		
		final ContentValues[] movieValues = MovieTable.getInstance().getContentValues(data);
		resolver.bulkInsert(RottenTomatoesContentProvider.Uris.MOVIES_URI, movieValues);
		
		final String whereClause = MovieTypeTable.Columns.TYPE + "=?";
		final String[] whereArgs = new String[] { mType };
		resolver.delete(RottenTomatoesContentProvider.Uris.MOVIE_TYPES_URI, whereClause, whereArgs);

		final ContentValues[] movieTypeValues = MovieTypeTable.getInstance().getContentValues(data, mType);
		resolver.bulkInsert(RottenTomatoesContentProvider.Uris.MOVIE_TYPES_URI, movieTypeValues);
		
		RottenTomatoesUriCache.add(Uri.withAppendedPath(RottenTomatoesContentProvider.Uris.MOVIE_TYPES_URI, mType));
	}
}
