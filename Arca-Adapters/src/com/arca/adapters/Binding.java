package com.arca.adapters;

import android.database.Cursor;

public class Binding {

	private final int mType;
	private final int mViewId; 
	private final String mColumnName;
	private int mColumnIndex = -1;
	
	public Binding(final int viewId, final String columnName) {
		this(0, viewId, columnName);
	}
	
	public Binding(final int type, final int viewId, final String columnName) {
		mType = type;
		mViewId = viewId;
		mColumnName = columnName;
	}
	
	public boolean isType(int type) {
		return mType == type;
	}

	public int getType() {
		return mType;
	}

	public int getViewId() {
		return mViewId;
	}

	public String getColumnName() {
		return mColumnName;
	}
	
	public int getColumnIndex() {
		return mColumnIndex;
	}
	
	public void findColumnIndex(final Cursor cursor) {
		if (mColumnIndex < 0) {
			mColumnIndex = cursor.getColumnIndexOrThrow(getColumnName());
		}
	}
}
