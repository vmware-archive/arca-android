package com.arca.adapters.test;

import android.database.MatrixCursor;
import android.test.AndroidTestCase;

import com.arca.adapters.Binding;

public class BindingTest extends AndroidTestCase {

	public void testBindingViewId() {
		final Binding binding = new Binding(0, "_id");
		assertEquals(0, binding.getViewId());
	}
	
	public void testBindingColumnName() {
		final Binding binding = new Binding(0, "_id");
		assertEquals("_id", binding.getColumnName());
	}
	
	public void testBindingColumnDefaultType() {
		final Binding binding = new Binding(0, "_id");
		assertEquals(0, binding.getType());
	}
	
	public void testBindingColumnCustomType() {
		final Binding binding = new Binding(1, 0, "_id");
		assertEquals(1, binding.getType());
	}
	
	public void testBindingColumnIsType() {
		final Binding binding = new Binding(1, 0, "_id");
		assertTrue(binding.isType(1));
	}
	
	public void testBindingFindColumnIndex() {
		final String[] columns = new String[] { "_id" };
		final Binding binding = new Binding(0, columns[0]);
		binding.findColumnIndex(new MatrixCursor(columns));
		assertEquals(0, binding.getColumnIndex());
	}
	
}
