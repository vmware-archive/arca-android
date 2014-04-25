package com.xtreme.rest.loader;

import android.database.Cursor;
import android.os.Bundle;

import com.xtreme.rest.utils.CursorUtils;

public class ContentResponse {

	private final Cursor mCursor;
	
	public ContentResponse(final Cursor cursor) {
		mCursor = cursor;
	}

	public Cursor getCursor() {
		return mCursor;
	}
	
	public boolean isExecutingRemote() {
		return CursorUtils.isExecutingRemote(mCursor);
	}
	
	public ContentState getContentState() {
		return CursorUtils.getContentState(mCursor);
	}
	
	public Bundle getExtras() {
		return CursorUtils.getExtras(mCursor);
	}
	
}
