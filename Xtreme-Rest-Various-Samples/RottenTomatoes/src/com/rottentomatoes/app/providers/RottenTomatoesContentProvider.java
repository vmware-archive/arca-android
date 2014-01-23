package com.rottentomatoes.app.providers;

import android.net.Uri;

import com.rottentomatoes.app.datasets.MovieTable;
import com.rottentomatoes.app.datasets.MovieTypeTable;
import com.rottentomatoes.app.datasets.MovieTypeView;
import com.xtreme.rest.RestContentProvider;

public class RottenTomatoesContentProvider extends RestContentProvider {
	
	public static final String AUTHORITY = "com.rottentomatoes.app.providers.RottenTomatoesContentProvider";
	
	public static final class Uris {
		public static final Uri MOVIES_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.MOVIES);
		public static final Uri MOVIE_TYPES_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.MOVIE_TYPES);
	}
	
	protected static final class Paths {
		public static final String MOVIES = "movies";
		public static final String MOVIE_TYPES = "movie_types";
	}
	
	@Override
	public boolean onCreate() {
		super.onCreate();
		registerDataset(AUTHORITY, Paths.MOVIES, MovieTable.class);
		registerDataset(AUTHORITY, Paths.MOVIE_TYPES, MovieTypeTable.class);
		registerDataset(AUTHORITY, Paths.MOVIE_TYPES + "/*", MovieTypeView.class);
		return true;
	}

}
