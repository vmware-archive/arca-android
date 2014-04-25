package com.xtreme.rest.binders;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class TextViewBinder implements ViewBinder {
	
	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
		if (view instanceof TextView) {
			return setViewText((TextView) view, cursor, columnIndex);
		}
		return false;
	}

	private static boolean setViewText(final TextView textView, final Cursor cursor, final int columnIndex) {
		final String text = cursor.getString(columnIndex);
		textView.setText(text);
		return true;
	}
}
