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
package com.arca.provider.test;

import android.test.AndroidTestCase;

import com.arca.provider.DatasetUtils;
import com.arca.provider.SQLiteTable;

public class ColumnUtilsTest extends AndroidTestCase {

	public void testColumnUtilsDefaultConversion() {
		final String columns = DatasetUtils.getColumns(new TestSQLiteTable());
		final String expected = "_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0";
		assertEquals(expected, columns);
	}

	// ====================================

	public static final class TestSQLiteTable extends SQLiteTable {

		public static interface Columns extends SQLiteTable.Columns {
		}
	}
}