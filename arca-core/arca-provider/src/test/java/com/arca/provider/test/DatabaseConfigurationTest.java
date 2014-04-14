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

import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.test.AndroidTestCase;

import com.arca.provider.DatabaseConfiguration;
import com.arca.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;

public class DatabaseConfigurationTest extends AndroidTestCase {

	public void testDatabaseConfigPackageName() {
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		assertEquals("com.arca.provider.test.db", config.getDatabaseName());
	}

	public void testDatabaseConfigVersion() {
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		assertEquals(1, config.getDatabaseVersion());
	}

	public void testDatabaseCursorFactory() {
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		final CursorFactory factory = config.getCursorFactory();
		assertNull(factory);
	}

	public void testDatabaseErrorHandler() {
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		final DatabaseErrorHandler handler = config.getErrorHandler();
		assertNull(handler);
	}

}
