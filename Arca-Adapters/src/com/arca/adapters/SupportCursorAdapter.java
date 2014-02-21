package com.arca.adapters;


import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;

import com.arca.adapters.ViewBinder.DefaultViewBinder;

public class SupportCursorAdapter extends ResourceCursorAdapter {
	
	private final DefaultViewBinder mDefaultBinder;
	private final CursorAdapterHelper mHelper;
	
	private ViewBinder mViewBinder;
	
	public SupportCursorAdapter(final Context context, final int layout, final Collection<Binding> bindings) {
		super(context, layout, null, 0);
		mHelper = new CursorAdapterHelper(bindings);
		mDefaultBinder = new DefaultViewBinder();
	}
	
	public void setViewBinder(final ViewBinder binder) {
		mViewBinder = binder;
	}
	
	public boolean hasResults() {
		final Cursor cursor = getCursor();
		return cursor != null && cursor.getCount() > 0;
	}
	
	@Override
	public void bindView(final View container, final Context context, final Cursor cursor) {
		final List<Binding> bindings = getBindings(cursor); 
		
		for (final Binding binding : bindings) {
			bindView(container, cursor, binding);
		}
	}
	
	private List<Binding> getBindings(final Cursor cursor) {
		final int type = getItemViewType(cursor.getPosition());
		final List<Binding> bindings = mHelper.getBindings(type, cursor);
		return bindings;
	}

	private void bindView(final View container, final Cursor cursor, final Binding binding) {
		final View view = mHelper.getView(container, binding);

		boolean bound = false;
		
		if (mViewBinder != null) {
			bound = mViewBinder.setViewValue(view, cursor, binding);
		}
		
		if (!bound) {
			bound = mDefaultBinder.setViewValue(view, cursor, binding);
		}
		
		if (!bound) {
			throw new IllegalStateException("Connot bind to view: " + view);
		}
	}
}
