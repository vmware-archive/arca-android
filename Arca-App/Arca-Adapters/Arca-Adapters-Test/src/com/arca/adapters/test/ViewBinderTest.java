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

import android.database.MatrixCursor;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

import com.arca.adapters.Binding;
import com.arca.adapters.ViewBinder.DefaultViewBinder;

public class ViewBinderTest extends AndroidTestCase {

	public void testViewBinderSucceedsBindingTextView() {
		final String[] columns = new String[] { "_id" };
		final MatrixCursor cursor = createCursor(columns);
		final Binding binding = createBinding(columns, cursor);
		final TextView textView = new TextView(getContext());
		final DefaultViewBinder binder = new DefaultViewBinder();
		binder.setViewValue(textView, cursor, binding);
		assertEquals("test", textView.getText());
	}

	public void testViewBinderSucceedsBindingView() {
		final String[] columns = new String[] { "_id" };
		final MatrixCursor cursor = createCursor(columns);
		final Binding binding = createBinding(columns, cursor);
		final View view = new TextView(getContext());
		final DefaultViewBinder binder = new DefaultViewBinder();
		final boolean bound = binder.setViewValue(view, cursor, binding);
		assertTrue(bound);
	}

	public void testViewBinderFailsBindingView() {
		final String[] columns = new String[] { "_id" };
		final MatrixCursor cursor = createCursor(columns);
		final Binding binding = createBinding(columns, cursor);
		final View view = new View(getContext());
		final DefaultViewBinder binder = new DefaultViewBinder();
		final boolean bound = binder.setViewValue(view, cursor, binding);
		assertFalse(bound);
	}

	private static Binding createBinding(final String[] columns, final MatrixCursor cursor) {
		final Binding binding = new Binding(0, columns[0]);
		binding.findColumnIndex(cursor);
		return binding;
	}

	private static MatrixCursor createCursor(final String[] columns) {
		final MatrixCursor cursor = new MatrixCursor(columns);
		cursor.addRow(new String[] { "test" });
		cursor.moveToFirst();
		return cursor;
	}

}
