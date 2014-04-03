package com.crunchbase.app.datasets;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.arca.provider.Column;
import com.arca.provider.Column.Type;
import com.arca.provider.SQLiteTable;
import com.arca.provider.Unique;
import com.arca.provider.Unique.OnConflict;
import com.arca.utils.ArrayUtils;
import com.arca.utils.StringUtils;
import com.crunchbase.app.models.Company;

public class CompanyTable extends SQLiteTable {
	
	public static interface ColumnNames {
		public static final String NAME = "name";
        public static final String CATEGORY_CODE = "category_code";
        public static final String DESCRIPTION = "description";
        public static final String PERMALINK = "permalink";
        public static final String CRUNCHBASE_URL = "crunchbase_url";
        public static final String HOMEPAGE_URL = "homepage_url";
        public static final String NAMESPACE = "namespace";
        public static final String OVERVIEW = "overview";
        public static final String IMAGE_URL = "image_url";
	}
	
	public static interface Columns extends SQLiteTable.Columns {
		@Unique(OnConflict.REPLACE)
        public static final Column NAME = Type.TEXT.newColumn(ColumnNames.NAME);
        public static final Column CATEGORY_CODE = Type.TEXT.newColumn(ColumnNames.CATEGORY_CODE);
        public static final Column DESCRIPTION = Type.TEXT.newColumn(ColumnNames.DESCRIPTION);
        public static final Column PERMALINK = Type.TEXT.newColumn(ColumnNames.PERMALINK);
        public static final Column CRUNCHBASE_URL = Type.TEXT.newColumn(ColumnNames.CRUNCHBASE_URL);
        public static final Column HOMEPAGE_URL = Type.TEXT.newColumn(ColumnNames.HOMEPAGE_URL);
        public static final Column NAMESPACE = Type.TEXT.newColumn(ColumnNames.NAMESPACE);
        public static final Column OVERVIEW = Type.TEXT.newColumn(ColumnNames.OVERVIEW);
        public static final Column IMAGE_URL = Type.TEXT.newColumn(ColumnNames.IMAGE_URL);
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

