package com.xtreme.rest.test.junit.mock.providers;

import android.content.ContentValues;

import com.xtreme.rest.providers.SQLView;

public class TestView extends SQLView {

	public static final String VIEW_NAME = "test_view";

	public static final class Columns {
		public static final String ID = TestTable1.Columns.ID;
		public static final String TEXT1 = TestTable1.Columns.TEXT1;
		public static final String TEXT2 = TestTable1.Columns.TEXT2;
		public static final String TEXT3 = TestTable1.Columns.TEXT3;
		public static final String ID2 = TestTable2.Columns.ID2;
		public static final String TEXT4 = TestTable2.Columns.TEXT4;
		public static final String TEXT5 = TestTable2.Columns.TEXT5;
		public static final String TEXT6 = TestTable2.Columns.TEXT6;
	}

	@Override
	public String getName() {
		return VIEW_NAME;
	}
	
	@Override
	protected String onCreateSelectStatement() {
		return TestTable1.TABLE_NAME + " LEFT JOIN " + TestTable2.TABLE_NAME + " ON " + TestTable1.TABLE_NAME + "." + TestTable1.Columns.ID + " = " + TestTable2.TABLE_NAME + "." + TestTable2.Columns.ID2;
	}
	
	public static final ContentValues[] CONTENT_ARRAY;
	
	static {
		
		CONTENT_ARRAY = new ContentValues[5];
		CONTENT_ARRAY[0] = new ContentValues();
		CONTENT_ARRAY[0].putAll(TestTable1.CONTENT_VALUES0);
		CONTENT_ARRAY[0].putAll(TestTable2.CONTENT_VALUES0);
		
		CONTENT_ARRAY[1] = new ContentValues();
		CONTENT_ARRAY[1].putAll(TestTable1.CONTENT_VALUES1);
		CONTENT_ARRAY[1].putAll(TestTable2.CONTENT_VALUES1);
		
		CONTENT_ARRAY[2] = new ContentValues();
		CONTENT_ARRAY[2].putAll(TestTable1.CONTENT_VALUES2);
		CONTENT_ARRAY[2].putAll(TestTable2.CONTENT_VALUES2);
		
		CONTENT_ARRAY[3] = new ContentValues();
		CONTENT_ARRAY[3].putAll(TestTable1.CONTENT_VALUES3);
		CONTENT_ARRAY[3].putAll(TestTable2.CONTENT_VALUES3);
		
		CONTENT_ARRAY[4] = new ContentValues();
		CONTENT_ARRAY[4].putAll(TestTable1.CONTENT_VALUES4);
		CONTENT_ARRAY[4].putAll(TestTable2.CONTENT_VALUES4);
	}

}
