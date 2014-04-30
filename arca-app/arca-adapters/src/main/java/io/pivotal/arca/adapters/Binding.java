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
package io.pivotal.arca.adapters;

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
