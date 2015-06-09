/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.provider;

import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.test.AndroidTestCase;

import io.pivotal.arca.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;

public class DatabaseConfigurationTest extends AndroidTestCase {

	public void testDatabaseConfigPackageName() {
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		assertEquals("io.pivotal.arca.provider.test.db", config.getDatabaseName());
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
