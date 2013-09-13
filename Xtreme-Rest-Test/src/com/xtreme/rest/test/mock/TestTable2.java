package com.xtreme.rest.test.mock;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.xtreme.rest.providers.SQLTable;

public class TestTable2 extends SQLTable {

	public static final String TABLE_NAME = "test_table2";

	public static final class Columns {
		public static final String ID2 = "id2";
		public static final String TEXT4 = "text4";
		public static final String TEXT5 = "text5";
		public static final String TEXT6 = "text6";
	}

	@Override
	public String getName() {
		return TABLE_NAME;
	}
	
	@Override
	protected Map<String, String> onCreateColumnMapping() {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(BaseColumns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		map.put(Columns.ID2, "BIGINT");
		map.put(Columns.TEXT4, "TEXT");
		map.put(Columns.TEXT5, "TEXT");
		map.put(Columns.TEXT6, "TEXT");
		return map;
	}
	
	@Override
	protected String onCreateUniqueConstraint() {
		return "UNIQUE (" + Columns.ID2 + ") ON CONFLICT REPLACE";
	}
	
	public static final ContentValues CONTENT_VALUES0;
	public static final ContentValues CONTENT_VALUES1;
	public static final ContentValues CONTENT_VALUES2;
	public static final ContentValues CONTENT_VALUES3;
	public static final ContentValues CONTENT_VALUES4;
	public static final ContentValues[] CONTENT_ARRAY;
	
	static {
		
		CONTENT_VALUES0 = new ContentValues();
		CONTENT_VALUES0.put(Columns.ID2, 100);
		CONTENT_VALUES0.put(Columns.TEXT4, "test400");
		CONTENT_VALUES0.put(Columns.TEXT5, "test500");
		CONTENT_VALUES0.put(Columns.TEXT6, "test600");

		CONTENT_VALUES1 = new ContentValues();
		CONTENT_VALUES1.put(Columns.ID2, 101);
		CONTENT_VALUES1.put(Columns.TEXT4, "test401");
		CONTENT_VALUES1.put(Columns.TEXT5, "test501");
		CONTENT_VALUES1.put(Columns.TEXT6, "test601");
		
		CONTENT_VALUES2 = new ContentValues();
		CONTENT_VALUES3 = new ContentValues();
		CONTENT_VALUES4 = new ContentValues();
		
		CONTENT_ARRAY = new ContentValues[2];
		CONTENT_ARRAY[0] = CONTENT_VALUES0;
		CONTENT_ARRAY[1] = CONTENT_VALUES1;
		
	}

}
