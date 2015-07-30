/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
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
package io.pivotal.arca.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Collection;

public class RecyclerViewCursorAdapterTest extends AndroidTestCase {

	public void testSupportCursorAdapterNullCursorHasNoResults() {
		final TestRecyclerCursorAdapter adapter = new TestRecyclerCursorAdapter(getContext(), null, null);
		assertFalse(adapter.hasResults());
	}

	public void testSupportCursorAdapterEmptyCursorHasNoResults() {
		final MatrixCursor cursor = new MatrixCursor(new String[] { "_id" });
		final TestRecyclerCursorAdapter adapter = new TestRecyclerCursorAdapter(getContext(), null, cursor);
		assertFalse(adapter.hasResults());
	}

	public void testSupportCursorAdapterCursorHasResults() {
		final Cursor cursor = createCursor(new String[] { "_id" });
		final TestRecyclerCursorAdapter adapter = new TestRecyclerCursorAdapter(getContext(), null, cursor);
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
		final TestRecyclerCursorAdapter adapter = new TestRecyclerCursorAdapter(getContext(), bindings, cursor);
		TestRecyclerCursorAdapter.TestBindingViewHolder viewHolder = adapter.onCreateViewHolder(container, 0);
		adapter.onBindViewHolder(viewHolder, 0);
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
		final TestRecyclerCursorAdapter adapter = new TestRecyclerCursorAdapter(getContext(), bindings, cursor, new TestViewBinder());
		TestRecyclerCursorAdapter.TestBindingViewHolder viewHolder = adapter.onCreateViewHolder(container, 0);
		adapter.onBindViewHolder(viewHolder, 0);
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
			final TestRecyclerCursorAdapter adapter = new TestRecyclerCursorAdapter(getContext(), bindings, cursor);
			TestRecyclerCursorAdapter.TestBindingViewHolder viewHolder = adapter.onCreateViewHolder(container, 0);
			adapter.onBindViewHolder(viewHolder, 0);
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

	public class TestRecyclerCursorAdapter extends RecyclerViewCursorAdapter<TestRecyclerCursorAdapter.TestBindingViewHolder> {

		public TestRecyclerCursorAdapter(Context context, final Collection<Binding> bindings, Cursor cursor) {
			super(context, bindings, cursor);
		}

		public TestRecyclerCursorAdapter(Context context, final Collection<Binding> bindings, Cursor cursor, ViewBinder viewBinder) {
			super(context, bindings, cursor, viewBinder);
		}

		@Override
		public TestBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new TestBindingViewHolder(parent, mBindings);
		}

		class TestBindingViewHolder extends RecyclerViewCursorAdapter.ArcaBindingRecyclerViewHolder {

			public TestBindingViewHolder(View itemView, Collection<Binding> bindings) {
				super(itemView, bindings);
			}
		}
	}

}
