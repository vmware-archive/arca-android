package com.crunchbase.app.datasets;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.crunchbase.app.models.Company;
import com.xtreme.rest.provider.SQLTable;

public abstract class AbsCompanyTable extends SQLTable {
	
	public static final String TABLE_NAME = "companies";
	
	protected static class Columns {
        public static final String NAME = Company.Fields.NAME;
        public static final String CATEGORY_CODE = Company.Fields.CATEGORY_CODE;
        public static final String DESCRIPTION = Company.Fields.DESCRIPTION;
        public static final String PERMALINK = Company.Fields.PERMALINK;
        public static final String CRUNCHBASE_URL = Company.Fields.CRUNCHBASE_URL;
        public static final String HOMEPAGE_URL = Company.Fields.HOMEPAGE_URL;
        public static final String NAMESPACE = Company.Fields.NAMESPACE;
        public static final String OVERVIEW = Company.Fields.OVERVIEW;
	}
	
	@Override
	public String getName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> onCreateColumnMapping() {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(BaseColumns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
        map.put(Columns.NAME, "TEXT");
        map.put(Columns.CATEGORY_CODE, "TEXT");
        map.put(Columns.DESCRIPTION, "TEXT");
        map.put(Columns.PERMALINK, "TEXT");
        map.put(Columns.CRUNCHBASE_URL, "TEXT");
        map.put(Columns.HOMEPAGE_URL, "TEXT");
        map.put(Columns.NAMESPACE, "TEXT");
        map.put(Columns.OVERVIEW, "TEXT");
		return map;
	}
	
	public ContentValues[] getContentValues(final List<Company> list) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i));
		}
		return values;
    }
	
	public ContentValues getContentValues(final Company item) {
		final ContentValues value = new ContentValues();
        value.put(Columns.NAME, item.getName());
        value.put(Columns.CATEGORY_CODE, item.getCategoryCode());
        value.put(Columns.DESCRIPTION, item.getDescription());
        value.put(Columns.PERMALINK, item.getPermalink());
        value.put(Columns.CRUNCHBASE_URL, item.getCrunchbaseUrl());
        value.put(Columns.HOMEPAGE_URL, item.getHomepageUrl());
        value.put(Columns.NAMESPACE, item.getNamespace());
        value.put(Columns.OVERVIEW, item.getOverview());
        return value;
    }
	
}
