package com.crunchbase.app.datasets;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.crunchbase.app.models.Company;
import com.xtreme.rest.utils.ArrayUtils;
import com.xtreme.rest.utils.StringUtils;

public class CompanyTable extends AbsCompanyTable {
	
	private static final class Holder { 
        public static final CompanyTable INSTANCE = new CompanyTable();
	}
	
	public static synchronized CompanyTable getInstance() {
	    return Holder.INSTANCE;
	}
	
	public static final class Columns extends AbsCompanyTable.Columns {
		public static final String IMAGE_URL = "image_url";
	}
	
	private CompanyTable() {}

	@Override
	protected Map<String, String> onCreateColumnMapping() {
	    final Map<String, String> map = super.onCreateColumnMapping();
	    map.put(Columns.IMAGE_URL, "TEXT");
	    return map;
	}

	@Override
	public ContentValues getContentValues(final Company item) {
	    final ContentValues value = super.getContentValues(item);
	    value.put(Columns.IMAGE_URL, item.getImageUrl());
	    return value;
	}
	
	@Override
	protected String onCreateUniqueConstraint() {
		return "UNIQUE (" + Columns.NAME + ") ON CONFLICT REPLACE";
	}
	
	@Override
	public Cursor query(final SQLiteDatabase database, final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		if (uri.getPathSegments().size() > 1) { 
			final String selectionWithId = StringUtils.append(selection, Columns.NAME + "=?", " AND ");
			final String[] selectionArgsWithId = ArrayUtils.append(selectionArgs, new String[] { uri.getLastPathSegment() });
			return super.query(database, uri, projection, selectionWithId, selectionArgsWithId, sortOrder);
		} else {
			return super.query(database, uri, projection, selection, selectionArgs, sortOrder);
		}
	}

}

