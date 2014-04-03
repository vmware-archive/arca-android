package com.rottentomatoes.app.models;

import java.util.List;

import android.content.ContentValues;

import com.rottentomatoes.app.datasets.MovieTypeTable.Columns;

public class MovieType {

	public static ContentValues[] getContentValues(final List<Movie> list, final String type) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i), type);
		}
		return values;
	}
	
	public static ContentValues getContentValues(final Movie item, final String type) {
		final ContentValues value = new ContentValues();
		value.put(Columns.ID.name, item.getId());
		value.put(Columns.TYPE.name, type);
		return value;
	}
	
}
