package com.arca.adapters.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.FrameLayout;

import com.arca.adapters.Binding;
import com.arca.adapters.CursorAdapterHelper;

public class CursorAdapterHelperTest extends AndroidTestCase {

	public void testBindingsNull() {
		final String[] columns = new String[] { "_id" };
		final Cursor cursor = createCursor(columns);
		final CursorAdapterHelper helper = new CursorAdapterHelper(null);
		final List<Binding> bindings = helper.getBindings(0, cursor);
		assertEquals(0, bindings.size());
	}
	
	public void testBindingsEmpty() {
		final String[] columns = new String[] { "_id" };
		final Cursor cursor = createCursor(columns);
		final Collection<Binding> allBindings = new ArrayList<Binding>();
		final CursorAdapterHelper helper = new CursorAdapterHelper(allBindings);
		final List<Binding> bindings = helper.getBindings(0, cursor);
		assertEquals(0, bindings.size());
	}
	
	public void testBindingsSingleItem() {
		final String[] columns = new String[] { "_id" };
		final Cursor cursor = createCursor(columns);
		final Collection<Binding> allBindings = new ArrayList<Binding>();
		allBindings.add(new Binding(0, 0, columns[0]));
		final CursorAdapterHelper helper = new CursorAdapterHelper(allBindings);
		final List<Binding> bindings = helper.getBindings(0, cursor);
		assertEquals(1, bindings.size());
	}
	
	public void testBindingsMultipleItemsDifferentType() {
		final String[] columns = new String[] { "_id" };
		final Cursor cursor = createCursor(columns);
		final Collection<Binding> allBindings = new ArrayList<Binding>();
		allBindings.add(new Binding(0, 0, columns[0]));
		allBindings.add(new Binding(1, 0, columns[0]));
		final CursorAdapterHelper helper = new CursorAdapterHelper(allBindings);
		final List<Binding> bindings = helper.getBindings(0, cursor);
		assertEquals(1, bindings.size());
	}
	
	public void testBindingsMultipleItemsSameType() {
		final String[] columns = new String[] { "_id" };
		final Cursor cursor = createCursor(columns);
		final Collection<Binding> allBindings = new ArrayList<Binding>();
		allBindings.add(new Binding(0, 0, columns[0]));
		allBindings.add(new Binding(0, 0, columns[0]));
		final CursorAdapterHelper helper = new CursorAdapterHelper(allBindings);
		final List<Binding> bindings = helper.getBindings(0, cursor);
		assertEquals(2, bindings.size());
	}
	
	public void testGetViewFromViewGroupEmpty() {
		final FrameLayout container = new FrameLayout(getContext());
		final Binding binding = new Binding(R.id.test_id_1, "_id");
		final CursorAdapterHelper helper = new CursorAdapterHelper(null);
		final View view = helper.getView(container, binding);
		assertNull(view);
	}
	
	public void testGetViewFromViewGroupWithSingleItem() {
		final View child1 = new View(getContext());
		child1.setId(R.id.test_id_1);
		final FrameLayout container = new FrameLayout(getContext());
		container.addView(child1);
		final Binding binding = new Binding(R.id.test_id_1, "_id");
		final CursorAdapterHelper helper = new CursorAdapterHelper(null);
		final View view = helper.getView(container, binding);
		assertEquals(child1, view);
	}
	
	public void testGetViewFromViewGroupWithMultipleItems() {
		final View child1 = new View(getContext());
		child1.setId(R.id.test_id_1);
		final View child2 = new View(getContext());
		child2.setId(R.id.test_id_2);
		final FrameLayout container = new FrameLayout(getContext());
		container.addView(child1);
		container.addView(child2);
		final Binding binding = new Binding(R.id.test_id_1, "_id");
		final CursorAdapterHelper helper = new CursorAdapterHelper(null);
		final View view = helper.getView(container, binding);
		assertEquals(child1, view);
	}

	private static MatrixCursor createCursor(final String[] columns) {
		final MatrixCursor cursor = new MatrixCursor(columns);
		cursor.addRow(new String[] { "test" });
		cursor.moveToFirst();
		return cursor;
	}
}
