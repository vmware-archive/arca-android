/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arca.dispatcher;

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
