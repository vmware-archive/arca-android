package com.xtreme.rest.adapters;

import com.xtreme.rest.binders.TextViewBinder;
import com.xtreme.rest.binders.ViewBinder;

import android.database.Cursor;
import android.view.View;

/**
 * A wrapper around a {@link Cursor} with a single row that uses the {@link ViewBinder} interface.
 */
public class ItemCursorAdapter {

	private final TextViewBinder mDefaultBinder = new TextViewBinder();
	private final CursorAdapterHelper mHelper;
	
	private ViewBinder mViewBinder;
	
	private final View mView;
	private Cursor mCursor;
	
	public ItemCursorAdapter(final View view, final String[] columnNames, final int[] viewIds) {
		mHelper = new CursorAdapterHelper(columnNames, viewIds);
		mHelper.findViews(view);
		mView = view;
	}
	
	public void setViewBinder(final ViewBinder viewBinder) {
		mViewBinder = viewBinder;
	}
	
	/**
	 * @return <code>true</code> if the cursor is non-null and has at least 1 row, false otherwise
	 */
	public boolean hasResults() {
		return mCursor != null && mCursor.getCount() > 0;
	}
	
	public void swapCursor(final Cursor cursor) {
		mCursor = cursor;
		mHelper.findColumns(cursor);

		if (cursor != null && cursor.moveToPosition(0)) {
			bindView(mView, cursor);
		}
	}
	
	public Cursor getCursor() {
		return mCursor;
	}
	
	protected void bindView(final View container, final Cursor cursor) {
		
		final int count = mHelper.getViewCount();

		for (int i = 0; i < count; i++) {
			
			final int columnIndex = mHelper.getColumnIndex(i);
			final View view = mHelper.getView(container, i);
			
			boolean bound = false;
			
			if (mViewBinder != null) {
				bound = mViewBinder.setViewValue(view, cursor, columnIndex);
			}
			
			if (!bound) {
				bound = mDefaultBinder.setViewValue(view, cursor, columnIndex);
			}
			
			if (!bound) {
				throw new IllegalStateException("Connot bind to view: " + view.getClass());
			}
		}
	}
	
}
