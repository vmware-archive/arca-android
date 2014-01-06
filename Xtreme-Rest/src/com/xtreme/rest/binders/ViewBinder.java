package com.xtreme.rest.binders;

import android.database.Cursor;
import android.view.View;

public interface ViewBinder {
	public boolean setViewValue(View view, Cursor cursor, Binding binding);
}
