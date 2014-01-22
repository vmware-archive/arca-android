package com.xtreme.rest.dispatcher;

import android.database.ContentObserver;
import android.database.Cursor;

public class QueryResult extends Result<Cursor> {

	public QueryResult(final Cursor data) {
		super(data);
	}

	public QueryResult(final Error error) {
		super(error);
	}

	public void registerContentObserver(final ContentObserver observer) {
		final Cursor cursor = getResult();
		if (cursor != null) {
			cursor.registerContentObserver(observer);
		}
	}

	public void unregisterContentObserver(final ContentObserver observer) {
		final Cursor cursor = getResult();
		if (cursor != null) {
			cursor.unregisterContentObserver(observer);
		}
	}
	
	public boolean isClosed() {
		final Cursor cursor = getResult();
		if (cursor != null) {
			return cursor.isClosed();
		} else {
			return true;
		}
	}

	public void close() {
		final Cursor cursor = getResult();
		if (cursor != null) {
			cursor.close();
		}
	}

	public boolean isValid() {
		// TODO this should return the result from the validator?
		return true;
	}

}
