package com.rottentomatoes.app.models;

public class Movie extends AbsMovie {

	public static class Fields extends AbsMovie.Fields {}
	
	public String getImageUrl() {
	    final Posters posters = getPosters();
	    return posters != null ? posters.getDetailed() : null;
	}
}
