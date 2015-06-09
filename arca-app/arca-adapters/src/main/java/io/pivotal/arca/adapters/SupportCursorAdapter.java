/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.adapters;

import java.util.Collection;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;

public class SupportCursorAdapter extends ResourceCursorAdapter {

	private final CursorAdapterHelper mHelper;

	public SupportCursorAdapter(final Context context, final int layout, final Collection<Binding> bindings) {
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
