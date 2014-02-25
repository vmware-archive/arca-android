package com.arca.adapters;


import java.util.Collection;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.View;
import android.widget.ResourceCursorAdapter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernCursorAdapter extends ResourceCursorAdapter {
	
	private final CursorAdapterHelper mHelper;

	public ModernCursorAdapter(final Context context, final int layout, final Collection<Binding> bindings) {
		super(context, layout, null, 0);
		mHelper = new CursorAdapterHelper(bindings);
	}
	
	public void setViewBinder(final ViewBinder binder) {
		mHelper.setViewBinder(binder);
	}

	public boolean hasResults() {
		final Cursor cursor = getCursor();
		return cursor != null && cursor.getCount() > 0;
	}
	
	@Override
	public void bindView(final View container, final Context context, final Cursor cursor) {
		final int type = getItemViewType(cursor.getPosition());
		mHelper.bindView(container, context, cursor, type);
	}
}
