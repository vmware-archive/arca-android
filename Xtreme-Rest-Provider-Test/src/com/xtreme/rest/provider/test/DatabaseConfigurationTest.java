package com.xtreme.rest.provider.test;

import java.util.Locale;

import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.test.AndroidTestCase;

import com.xtreme.rest.provider.DatabaseConfiguration;
import com.xtreme.rest.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;
import com.xtreme.rest.provider.PackageUtils;

public class DatabaseConfigurationTest extends AndroidTestCase {

	public void testDatabaseConfigPackageName() {
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		final String packageName = PackageUtils.getPackageName(getContext());
		final String databaseName = String.format(Locale.getDefault(), "%s.db", packageName);
		assertEquals(databaseName, config.getDatabaseName());
	}
	
	public void testDatabaseConfigVersion() {
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		final int databaseVersion = PackageUtils.getVersionCode(getContext());
		assertEquals(databaseVersion, config.getDatabaseVersion());
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
