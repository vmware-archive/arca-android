/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.Collection;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernItemAdapter extends CursorAdapter {

	private final CursorAdapterHelper mHelper;

	public ModernItemAdapter(final Context context, final Collection<Binding> bindings) {
		super(context, null, 0);
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

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public View newDropDownView(final Context context, final Cursor cursor, final ViewGroup parent) {
		throw new UnsupportedOperationException();
	}

}
