package com.rottentomatoes.app.datasets;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.rottentomatoes.app.models.Movie;
import com.xtreme.rest.provider.SQLTable;

public abstract class AbsMovieTable extends SQLTable {
	
	public static final String TABLE_NAME = "movies";
	
	protected static class Columns {
        public static final String ID = Movie.Fields.ID;
        public static final String TITLE = Movie.Fields.TITLE;
        public static final String YEAR = Movie.Fields.YEAR;
        public static final String MPAA_RATING = Movie.Fields.MPAA_RATING;
        public static final String RUNTIME = Movie.Fields.RUNTIME;
        public static final String CRITICS_CONSENSUS = Movie.Fields.CRITICS_CONSENSUS;
        public static final String SYNOPSIS = Movie.Fields.SYNOPSIS;
	}
	
	@Override
	public String getName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> onCreateColumnMapping() {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(BaseColumns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
        map.put(Columns.ID, "TEXT");
        map.put(Columns.TITLE, "TEXT");
        map.put(Columns.YEAR, "TEXT");
        map.put(Columns.MPAA_RATING, "TEXT");
        map.put(Columns.RUNTIME, "TEXT");
        map.put(Columns.CRITICS_CONSENSUS, "TEXT");
        map.put(Columns.SYNOPSIS, "TEXT");
		return map;
	}
	
	public ContentValues[] getContentValues(final List<Movie> list) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i));
		}
		return values;
    }
	
	public ContentValues getContentValues(final Movie item) {
		final ContentValues value = new ContentValues();
        value.put(Columns.ID, item.getId());
        value.put(Columns.TITLE, item.getTitle());
        value.put(Columns.YEAR, item.getYear());
        value.put(Columns.MPAA_RATING, item.getMpaaRating());
        value.put(Columns.RUNTIME, item.getRuntime());
        value.put(Columns.CRITICS_CONSENSUS, item.getCriticsConsensus());
        value.put(Columns.SYNOPSIS, item.getSynopsis());
        return value;
    }
	
}
