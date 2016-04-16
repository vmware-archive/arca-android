package io.pivotal.arca.dispatcher;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

public class QueryResult extends Result<Cursor> {

	public QueryResult(final Cursor data) {
		super(data);
	}

	public QueryResult(final Error error) {
		super(error);
	}

	public void setNotificationUri(final ContentResolver resolver, final Uri uri) {
		final Cursor cursor = getData();
		if (cursor != null) {
			cursor.setNotificationUri(resolver, uri);
		}
	}

	public void registerContentObserver(final ContentObserver observer) {
		final Cursor cursor = getData();
		if (cursor != null) {
			cursor.registerContentObserver(observer);
		}
	}

	public void unregisterContentObserver(final ContentObserver observer) {
		final Cursor cursor = getData();
		if (cursor != null) {
			cursor.unregisterContentObserver(observer);
		}
	}

	public boolean isClosed() {
		final Cursor cursor = getData();
		if (cursor != null) {
			return cursor.isClosed();
		} else {
			return true;
		}
	}

	public void close() {
		final Cursor cursor = getData();
		if (cursor != null) {
			cursor.close();
		}
	}

}
