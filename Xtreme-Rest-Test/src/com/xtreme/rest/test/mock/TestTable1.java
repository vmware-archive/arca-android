package com.xtreme.rest.test.mock;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.xtreme.rest.providers.SQLTable;

public class TestTable1 extends SQLTable {

	public static final String TABLE_NAME = "test_table1";

	public static final class Columns {
		public static final String ID = "id";
		public static final String TEXT1 = "text1";
		public static final String TEXT2 = "text2";
		public static final String TEXT3 = "text3";
	}
	
	@Override
	public String getName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> onCreateColumnMapping() {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(BaseColumns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		map.put(Columns.ID, "BIGINT");
		map.put(Columns.TEXT1, "TEXT");
		map.put(Columns.TEXT2, "TEXT");
		map.put(Columns.TEXT3, "TEXT");
		return map;
	}
	
	@Override
	protected String onCreateUniqueConstraint() {
		return "UNIQUE (" + Columns.ID + ") ON CONFLICT REPLACE";
	}
	
	
	public static final ContentValues UPDATE_VALUES_PARTIAL;
	public static final ContentValues UPDATE_VALUES_FULL;
	public static final ContentValues CONTENT_VALUES;
	public static final ContentValues CONTENT_VALUES0;
	public static final ContentValues CONTENT_VALUES1;
	public static final ContentValues CONTENT_VALUES2;
	public static final ContentValues CONTENT_VALUES3;
	public static final ContentValues CONTENT_VALUES4;
	public static final ContentValues[] CONTENT_ARRAY;
	
	static {
		CONTENT_VALUES = new ContentValues();
		CONTENT_VALUES.put(Columns.ID, 1);
		CONTENT_VALUES.put(Columns.TEXT1, "test1");
		CONTENT_VALUES.put(Columns.TEXT2, "test2");
		CONTENT_VALUES.put(Columns.TEXT3, "test3");
		
		CONTENT_VALUES0 = new ContentValues();
		CONTENT_VALUES0.put(Columns.ID, 100);
		CONTENT_VALUES0.put(Columns.TEXT1, "test100");
		CONTENT_VALUES0.put(Columns.TEXT2, "test200");
		CONTENT_VALUES0.put(Columns.TEXT3, "test300");
		
		CONTENT_VALUES1 = new ContentValues();
		CONTENT_VALUES1.put(Columns.ID, 101);
		CONTENT_VALUES1.put(Columns.TEXT1, "test101");
		CONTENT_VALUES1.put(Columns.TEXT2, "test201");
		CONTENT_VALUES1.put(Columns.TEXT3, "test301");
		
		CONTENT_VALUES2 = new ContentValues();
		CONTENT_VALUES2.put(Columns.ID, 102);
		CONTENT_VALUES2.put(Columns.TEXT1, "test102");
		CONTENT_VALUES2.put(Columns.TEXT2, "test202");
		CONTENT_VALUES2.put(Columns.TEXT3, "test302");
		
		CONTENT_VALUES3 = new ContentValues();
		CONTENT_VALUES3.put(Columns.ID, 103);
		CONTENT_VALUES3.put(Columns.TEXT1, "test103");
		CONTENT_VALUES3.put(Columns.TEXT2, "test203");
		CONTENT_VALUES3.put(Columns.TEXT3, "test303");
		
		CONTENT_VALUES4 = new ContentValues();
		CONTENT_VALUES4.put(Columns.ID, 104);
		CONTENT_VALUES4.put(Columns.TEXT1, "test104");
		CONTENT_VALUES4.put(Columns.TEXT2, "test204");
		CONTENT_VALUES4.put(Columns.TEXT3, "test304");
		
		CONTENT_ARRAY = new ContentValues[5];
		CONTENT_ARRAY[0] = CONTENT_VALUES0;
		CONTENT_ARRAY[1] = CONTENT_VALUES1;
		CONTENT_ARRAY[2] = CONTENT_VALUES2;
		CONTENT_ARRAY[3] = CONTENT_VALUES3;
		CONTENT_ARRAY[4] = CONTENT_VALUES4;
		
		
		UPDATE_VALUES_PARTIAL = new ContentValues();
		UPDATE_VALUES_PARTIAL.put(Columns.TEXT1, "update100");
		
		UPDATE_VALUES_FULL = new ContentValues();
		UPDATE_VALUES_FULL.put(Columns.ID, 100);
		UPDATE_VALUES_FULL.put(Columns.TEXT1, "update100");
		UPDATE_VALUES_FULL.put(Columns.TEXT2, "update200");
		UPDATE_VALUES_FULL.put(Columns.TEXT3, "update300");
	}

}
