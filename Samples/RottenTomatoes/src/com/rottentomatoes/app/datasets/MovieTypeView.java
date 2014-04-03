package com.rottentomatoes.app.datasets;

import android.database.Cursor;
import android.net.Uri;

import com.arca.provider.SQLiteView;
import com.arca.provider.Select;
import com.arca.utils.ArrayUtils;
import com.arca.utils.StringUtils;

public class MovieTypeView extends SQLiteView {

	@Select("SELECT * FROM (MovieTable, MovieTypeTable USING (id))")
	public static interface Columns extends MovieTable.Columns, MovieTypeTable.Columns {}
	
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
