package io.pivotal.arca.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import java.util.Collection;
import java.util.List;

import io.pivotal.arca.adapters.ViewBinder.DefaultViewBinder;

public class CursorAdapterHelper {

	private final BindingHelper mBindingHelper;
	private final DefaultViewBinder mDefaultBinder;

	private ViewBinder mViewBinder;

	public CursorAdapterHelper(final Collection<Binding> bindings) {
		mBindingHelper = new BindingHelper(bindings);
		mDefaultBinder = new DefaultViewBinder();
	}

	public void setViewBinder(final ViewBinder binder) {
		mViewBinder = binder;
	}

	public void bindView(final View container, final Context context, final Cursor cursor, final int type) {
		final List<Binding> bindings = mBindingHelper.getBindings(type, cursor);

		for (final Binding binding : bindings) {
			bindView(container, cursor, binding);
		}
	}

	private void bindView(final View container, final Cursor cursor, final Binding binding) {
		final View view = ViewHelper.getView(container, binding.getViewId());

		boolean bound = false;

		if (mViewBinder != null) {
			bound = mViewBinder.setViewValue(view, cursor, binding);
		}

		if (!bound) {
			bound = mDefaultBinder.setViewValue(view, cursor, binding);
		}

		if (!bound) {
			throw new IllegalStateException("Cannot bind to view: " + view);
		}
	}
}
