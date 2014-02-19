package com.xtreme.rest.adapters.test;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xtreme.rest.adapters.Binding;
import com.xtreme.rest.adapters.ItemCursorAdapter;
import com.xtreme.rest.adapters.ViewBinder;

public class ItemCursorAdapterTest extends AndroidTestCase {

	public void testItemCursorAdapterNullCursorHasNoResults() {
		final ItemCursorAdapter adapter = new ItemCursorAdapter(null, null);
		assertFalse(adapter.hasResults());
	}
	
	public void testItemCursorAdapterEmptyCursorHasNoResults() {
		final MatrixCursor cursor = new MatrixCursor(new String[] { "_id" });
		final ItemCursorAdapter adapter = new ItemCursorAdapter(null, null);
		adapter.swapCursor(cursor);
		assertFalse(adapter.hasResults());
	}
	
	public void testItemCursorAdapterCursorHasResults() {
		final Cursor cursor = createCursor(new String[] { "_id" });
		final ItemCursorAdapter adapter = new ItemCursorAdapter(null, null);
		adapter.swapCursor(cursor);
		assertTrue(adapter.hasResults());
	}
	
	public void testItemCursorAdapterDefaultViewBinding() {
		final TextView child1 = new TextView(getContext());
		child1.setId(R.id.test_id_1);
		final FrameLayout container = new FrameLayout(getContext());
		container.addView(child1);
		final String[] columns = new String[] { "_id" };
		final Cursor cursor = createCursor(columns);
		final Collection<Binding> bindings = createBindings(columns);
		final ItemCursorAdapter adapter = new ItemCursorAdapter(container, bindings);
		adapter.bindViewAtPosition(container, cursor, 0);
		assertEquals("default_test", child1.getText());
	}
	
	public void testItemCursorAdapterCustomViewBinding() {
		final TextView child1 = new TextView(getContext());
		child1.setId(R.id.test_id_1);
		final FrameLayout container = new FrameLayout(getContext());
		container.addView(child1);
		final String[] columns = new String[] { "_id" };
		final Cursor cursor = createCursor(columns);
		final Collection<Binding> bindings = createBindings(columns);
		final ItemCursorAdapter adapter = new ItemCursorAdapter(container, bindings);
		adapter.setViewBinder(new TestViewBinder());
		adapter.bindViewAtPosition(container, cursor, 0);
		assertEquals("custom_test", child1.getText());
	}
	
	public void testItemCursorAdapterCannotBindView() {
		try {
			final View child1 = new View(getContext());
			child1.setId(R.id.test_id_1);
			final FrameLayout container = new FrameLayout(getContext());
			container.addView(child1);
			final String[] columns = new String[] { "_id" };
			final Cursor cursor = createCursor(columns);
			final Collection<Binding> bindings = createBindings(columns);
			final ItemCursorAdapter adapter = new ItemCursorAdapter(container, bindings);
			adapter.bindViewAtPosition(container, cursor, 0);
			Assert.fail();
		} catch (final IllegalStateException e) {
			assertNotNull(e);
		}
	}

	private static Collection<Binding> createBindings(final String[] columns) {
		final Collection<Binding> bindings = new ArrayList<Binding>();
		bindings.add(new Binding(R.id.test_id_1, columns[0]));
		return bindings;
	}
	
	private static MatrixCursor createCursor(final String[] columns) {
		final MatrixCursor cursor = new MatrixCursor(columns);
		cursor.addRow(new String[] { "default_test" });
		cursor.moveToFirst();
		return cursor;
	}
	
	public class TestViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
			((TextView) view).setText("custom_test");
			return true;
		}
	}
	
}
