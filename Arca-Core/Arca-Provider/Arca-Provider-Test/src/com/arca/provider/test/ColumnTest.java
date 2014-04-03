package com.arca.provider.test;

import android.test.AndroidTestCase;

import com.arca.provider.Column;

public class ColumnTest extends AndroidTestCase {

	public void testColumnTypeValueOfInteger() {
		assertEquals(Column.Type.INTEGER, Column.Type.valueOf("INTEGER"));
	}
	
	public void testColumnTypeValueOfReal() {
		assertEquals(Column.Type.REAL, Column.Type.valueOf("REAL"));
	}
	
	public void testColumnTypeValueOfText() {
		assertEquals(Column.Type.TEXT, Column.Type.valueOf("TEXT"));
	}
	
	public void testColumnTypeValueOfBlob() {
		assertEquals(Column.Type.BLOB, Column.Type.valueOf("BLOB"));
	}
	
	public void testColumnTypeValues() {
		final Column.Type[] types = Column.Type.values();
		assertEquals(Column.Type.INTEGER, types[0]);
		assertEquals(Column.Type.REAL, types[1]);
		assertEquals(Column.Type.TEXT, types[2]);
		assertEquals(Column.Type.BLOB, types[3]);
	}
	
	public void testColumnTypeInteger() {
		final String name = Column.Type.INTEGER.toString();
		assertEquals("INTEGER", name);
	}
	
	public void testColumnTypeReal() {
		final String name = Column.Type.REAL.toString();
		assertEquals("REAL", name);
	}
	
	public void testColumnTypeText() {
		final String name = Column.Type.TEXT.toString();
		assertEquals("TEXT", name);
	}
	
	public void testColumnTypeBlob() {
		final String name = Column.Type.BLOB.toString();
		assertEquals("BLOB", name);
	}

	public void testTextColumnWithCustomName() {
		final String name = "test";
		final Column column = Column.Type.TEXT.newColumn(name);
		assertEquals(name, column.name);
		assertEquals(name, column.toString());
		assertEquals(Column.Type.TEXT, column.type);
	}
}
