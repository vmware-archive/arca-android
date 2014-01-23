package com.rottentomatoes.app.datasets;

import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.provider.SQLView;
import com.xtreme.rest.utils.ArrayUtils;
import com.xtreme.rest.utils.StringUtils;

public class MovieTypeView extends SQLView {

	public static final String VIEW_NAME = "movie_view";

	public static class Columns {
        public static final String ID = MovieTable.Columns.ID;
        public static final String TITLE = MovieTable.Columns.TITLE;
        public static final String YEAR = MovieTable.Columns.YEAR;
        public static final String MPAA_RATING = MovieTable.Columns.MPAA_RATING;
        public static final String RUNTIME = MovieTable.Columns.RUNTIME;
        public static final String CRITICS_CONSENSUS = MovieTable.Columns.CRITICS_CONSENSUS;
        public static final String SYNOPSIS = MovieTable.Columns.SYNOPSIS;
        public static final String IMAGE_URL = MovieTable.Columns.IMAGE_URL;
        public static final String TYPE = MovieTypeTable.Columns.TYPE;
	}
	
	@Override
	public String getName() {
		return VIEW_NAME;
	}
	
	@Override
	protected String onCreateSelectStatement() {
		final String movieTableId = String.format("%s.%s", MovieTable.TABLE_NAME, MovieTable.Columns.ID);
		final String movieTypeTableId = String.format("%s.%s", MovieTypeTable.TABLE_NAME, MovieTable.Columns.ID);
		return String.format("%s LEFT JOIN %s ON %s = %s", MovieTable.TABLE_NAME, MovieTypeTable.TABLE_NAME, movieTableId, movieTypeTableId);
	}
	
	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		if (uri.getPathSegments().size() > 1) { 
			final String selectionWithType = StringUtils.append(selection, Columns.TYPE + "=?", " AND ");
			final String[] selectionArgsWithType = ArrayUtils.append(selectionArgs, new String[] { uri.getLastPathSegment() });
			return super.query(uri, projection, selectionWithType, selectionArgsWithType, sortOrder);
		} else {
			return super.query(uri, projection, selection, selectionArgs, sortOrder);
		}
	}
	
}
