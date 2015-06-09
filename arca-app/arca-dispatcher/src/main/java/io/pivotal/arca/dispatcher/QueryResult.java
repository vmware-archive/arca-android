/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
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
		final Cursor cursor = getResult();
		if (cursor != null) {
			cursor.setNotificationUri(resolver, uri);
		}
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

}
