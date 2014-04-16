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
package io.pivotal.arca.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public interface Dataset {

	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder);

	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs);

	public Uri insert(final Uri uri, final ContentValues values);

	public int bulkInsert(final Uri uri, final ContentValues[] values);

	public int delete(final Uri uri, final String selection, final String[] selectionArgs);

}
