package com.xtreme.rest.binders;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public interface ViewBinder {
	public boolean setViewValue(View view, Cursor cursor, Binding binding);
	
	public static class DefaultViewBinder implements ViewBinder {
		
		@Override
		public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
			if (view instanceof TextView) {
				return setViewValue((TextView) view, cursor, binding);
			}
			return false;
		}
		
		public boolean setViewValue(final TextView view, final Cursor cursor, final Binding binding) {
			final int columnIndex = binding.getColumnIndex();
			final String text = cursor.getString(columnIndex);
			view.setText(text);
			return true;
		}
	}
}
