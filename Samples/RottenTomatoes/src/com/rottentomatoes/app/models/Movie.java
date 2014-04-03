package com.rottentomatoes.app.models;

import com.arca.provider.ColumnName;
import com.rottentomatoes.app.datasets.MovieTable;

public class Movie extends AbsMovie {

	public static class Fields extends AbsMovie.Fields {}
	
	@ColumnName(MovieTable.ColumnNames.IMAGE_URL)
	public String getImageUrl() {
	    final Posters posters = getPosters();
	    return posters != null ? posters.getDetailed() : null;
	}
}
