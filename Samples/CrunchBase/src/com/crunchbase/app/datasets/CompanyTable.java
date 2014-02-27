package com.crunchbase.app.datasets;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.arca.provider.Column;
import com.arca.provider.Column.Type;
import com.arca.provider.ColumnUtils;
import com.arca.provider.SQLiteTable;
import com.arca.utils.ArrayUtils;
import com.arca.utils.StringUtils;
import com.crunchbase.app.models.Company;

public class CompanyTable extends SQLiteTable {
	
	public static interface Columns extends SQLiteTable.Columns {
        public static final Column NAME = Type.TEXT.newColumn("name");
        public static final Column CATEGORY_CODE = Type.TEXT.newColumn("categpry_code");
        public static final Column DESCRIPTION = Type.TEXT.newColumn("descrption");
        public static final Column PERMALINK = Type.TEXT.newColumn("permalink");
        public static final Column CRUNCHBASE_URL = Type.TEXT.newColumn("crunchbase_url");
        public static final Column HOMEPAGE_URL = Type.TEXT.newColumn("homepage_url");
        public static final Column NAMESPACE = Type.TEXT.newColumn("namespace");
        public static final Column OVERVIEW = Type.TEXT.newColumn("overview");
        public static final Column IMAGE_URL = Type.TEXT.newColumn("image_url");
	}
	
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String columns = ColumnUtils.toString(Columns.class);
		final String constraint = "UNIQUE (" + Columns.NAME + ") ON CONFLICT REPLACE";
		db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s, %s);", getName(), columns, constraint));
	}
	
	@Override
	public void onDrop(final SQLiteDatabase db) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getName()));
	}
	
	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		if (uri.getPathSegments().size() > 1) { 
			final String selectionWithId = StringUtils.append(selection, Columns.NAME + "=?", " AND ");
			final String[] selectionArgsWithId = ArrayUtils.append(selectionArgs, new String[] { uri.getLastPathSegment() });
			return super.query(uri, projection, selectionWithId, selectionArgsWithId, sortOrder);
		} else {
			return super.query(uri, projection, selection, selectionArgs, sortOrder);
		}
	}
	
	public static ContentValues[] getContentValues(final List<Company> list) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i));
		}
		return values;
    }
	
	public static ContentValues getContentValues(final Company item) {
		final ContentValues value = new ContentValues();
        value.put(Columns.NAME.name, item.getName());
        value.put(Columns.CATEGORY_CODE.name, item.getCategoryCode());
        value.put(Columns.DESCRIPTION.name, item.getDescription());
        value.put(Columns.PERMALINK.name, item.getPermalink());
        value.put(Columns.CRUNCHBASE_URL.name, item.getCrunchbaseUrl());
        value.put(Columns.HOMEPAGE_URL.name, item.getHomepageUrl());
        value.put(Columns.NAMESPACE.name, item.getNamespace());
        value.put(Columns.OVERVIEW.name, item.getOverview());
        return value;
    }

}

