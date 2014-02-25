package com.arca.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.database.Cursor;
import android.util.SparseArray;

class BindingHelper {

	private final BindingHelper.BindingTypes mBindingTypes = new BindingTypes();
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