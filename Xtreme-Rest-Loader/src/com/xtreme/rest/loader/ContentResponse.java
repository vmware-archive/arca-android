package com.xtreme.rest.loader;

import android.database.Cursor;
import android.os.Bundle;

public class ContentResponse {

	private final Cursor mCursor;
	
	public ContentResponse(final Cursor cursor) {
		mCursor = cursor;
	}

	public Cursor getCursor() {
		return mCursor;
	}
	
	public Bundle getExtras() {
		return CursorUtils.getExtras(mCursor);
	}
	
}
