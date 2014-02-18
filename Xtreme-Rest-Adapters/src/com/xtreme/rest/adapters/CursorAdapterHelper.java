package com.xtreme.rest.adapters;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.database.Cursor;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;

public class CursorAdapterHelper {

	private final BindingHelper mBindingHelper;

	public CursorAdapterHelper(final Collection<Binding> bindings) {
		mBindingHelper = new BindingHelper(bindings);
	}

	public List<Binding> getBindings(final int type, final Cursor cursor) {
		return mBindingHelper.getBindings(type, cursor);
	}

	public View getView(final View container, final Binding binding) {
		return ViewHelper.getView(container, binding.getViewId());
	}

	private static final class BindingHelper {

		private final BindingTypes mBindingTypes = new BindingTypes();
		private final Collection<Binding> mBindings;

		public BindingHelper(final Collection<Binding> bindings) {
			mBindings = bindings;
		}

		public List<Binding> getBindings(final int type, final Cursor cursor) {
			final List<Binding> bindings = mBindingTypes.get(type);
			if (bindings == null) {
				return setupBindingsTypes(type, cursor);
			} else {
				return bindings;
			}
		}

		private List<Binding> setupBindingsTypes(final int type, final Cursor cursor) {
			for (final Binding binding : mBindings) {
				if (binding.isType(type)) { 
					binding.findColumnIndex(cursor);
					mBindingTypes.add(binding);
				}
			}
			return getNonNullBindings(type);
		}

		private List<Binding> getNonNullBindings(final int type) {
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

	private static final class ViewHelper {

		public static View getView(final View container, final int viewId) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				return getViewFromHolder(container, viewId);
			} else {
				return getViewFromTag(container, viewId);
			}
		}

		private static View getViewFromTag(final View container, final int viewId) {
			final View view = (View) container.getTag(viewId);
			if (view == null) {
				return getViewAndSetTag(container, viewId);
			} else {
				return view;
			}
		}

		private static View getViewAndSetTag(final View container, final int viewId) {
			final View view = container.findViewById(viewId);
			container.setTag(viewId, view);
			return view;
		}

		private static View getViewFromHolder(final View container, final int viewId) {
			final ViewHolder holder = (ViewHolder) container.getTag();
			if (holder == null) {
				return getViewAndCreateHolder(container, viewId);
			} else {
				return getViewFromHolder(container, viewId, holder);
			}
		}

		private static View getViewAndCreateHolder(final View container, final int viewId) {
			final ViewHolder holder = new ViewHolder(container);
			return getViewAndAddToHolder(container, viewId, holder);
		}

		private static View getViewAndAddToHolder(final View container, final int viewId, final ViewHolder holder) {
			final View view = container.findViewById(viewId);
			holder.put(viewId, view);
			return view;
		}
		
		private static View getViewFromHolder(final View container, final int viewId, final ViewHolder holder) {
			final View view = holder.get(viewId);
			if (view == null) {
				return getViewAndAddToHolder(container, viewId, holder);
			} else {
				return view;
			}
		}

		private static final class ViewHolder extends SparseArray<View> {
			public ViewHolder(final View container) {
				container.setTag(this);
			}
		}
	}
}
