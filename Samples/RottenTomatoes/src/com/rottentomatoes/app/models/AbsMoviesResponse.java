package com.rottentomatoes.app.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public abstract class AbsMoviesResponse  {

	protected static class Fields {
		public static final String MOVIES = "movies";
	}

    @SerializedName(Fields.MOVIES) private List<Movie> mMovies;

    public List<Movie> getMovies() {
        return mMovies;
    }
    
    public void setMovies(final List<Movie> movies) {
        mMovies = movies;
    }
    
}
