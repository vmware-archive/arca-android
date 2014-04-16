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
package io.pivotal.arca.adapters.test;

import android.database.MatrixCursor;
import android.test.AndroidTestCase;

import io.pivotal.arca.adapters.Binding;

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
