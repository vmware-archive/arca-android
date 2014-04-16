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
package io.pivotal.arca.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.database.Cursor;
import android.util.SparseArray;

class BindingHelper {

	private final BindingTypes mBindingTypes = new BindingTypes();
	private final Collection<Binding> mBindings;

	public BindingHelper(final Collection<Binding> bindings) {
		mBindings = bindings;
	}

	public List<Binding> getBindings(final int type, final Cursor cursor) {
		final List<Binding> bindings = mBindingTypes.get(type);
		if (bindings == null) {
			return setupBindingsOfType(type, cursor);
		} else {
			return bindings;
		}
	}

	private List<Binding> setupBindingsOfType(final int type, final Cursor cursor) {
		if (mBindings != null) {
			findBindingsOfType(type, cursor);
		}
		return getNonNullBindingsOfType(type);
	}

	private void findBindingsOfType(final int type, final Cursor cursor) {
		for (final Binding binding : mBindings) {
			if (binding.isType(type)) {
				binding.findColumnIndex(cursor);
				mBindingTypes.add(binding);
			}
		}
	}

	private List<Binding> getNonNullBindingsOfType(final int type) {
		final List<Binding> bindings = mBindingTypes.get(type);
		if (bindings == null) {
			return new ArrayList<Binding>();
		} else {
			return bindings;
		}
	}

	private static final class BindingTypes extends SparseArray<List<Binding>> {
		public void add(final Binding binding) {
			final int type = binding.getType();
			if (get(type) == null) {
				put(type, new ArrayList<Binding>());
			}
			get(type).add(binding);
		}
	}
}