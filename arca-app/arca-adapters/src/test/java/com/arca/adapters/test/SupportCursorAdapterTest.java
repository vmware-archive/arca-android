/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arca.adapters.test;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.arca.adapters.Binding;
import com.arca.adapters.R;
import com.arca.adapters.SupportCursorAdapter;
import com.arca.adapters.ViewBinder;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Collection;

public class SupportCursorAdapterTest extends AndroidTestCase {

	public void testSupportCursorAdapterNullCursorHasNoResults() {
		final SupportCursorAdapter adapter = new SupportCursorAdapter(getContext(), -1, null);
		assertFalse(adapter.hasResults());
	}

	public void testSupportCursorAdapterEmptyCursorHasNoResults() {
		final MatrixCursor cursor = new MatrixCursor(new String[] { "_id" });
		final SupportCursorAdapter adapter = new SupportCursorAdapter(getContext(), -1, null);
		adapter.swapCursor(cursor);
		assertFalse(adapter.hasResults());
	}

	public void testSupportCursorAdapterCursorHasResults() {
		final Cursor cursor = createCursor(new String[] { "_id" });
		final SupportCursorAdapter adapter = new SupportCursorAdapter(getContext(), -1, null);
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
		final SupportCursorAdapter adapter = new SupportCursorAdapter(getContext(), -1, bindings);
		adapter.bindView(container, getContext(), cursor);
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
		final SupportCursorAdapter adapter = new SupportCursorAdapter(getContext(), -1, bindings);
		adapter.setViewBinder(new TestViewBinder());
		adapter.bindView(container, getContext(), cursor);
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
			final SupportCursorAdapter adapter = new SupportCursorAdapter(getContext(), -1, bindings);
			adapter.bindView(container, getContext(), cursor);
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
