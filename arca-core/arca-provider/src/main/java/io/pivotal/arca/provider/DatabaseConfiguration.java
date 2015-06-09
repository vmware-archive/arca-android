/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Build;

import java.util.Locale;

public interface DatabaseConfiguration {

	public String getDatabaseName();

	public int getDatabaseVersion();

	public CursorFactory getCursorFactory();

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public DatabaseErrorHandler getErrorHandler();

	public static class DefaultDatabaseConfiguration implements DatabaseConfiguration {

		private final Context mContext;

		public DefaultDatabaseConfiguration(final Context context) {
			mContext = context;
		}

		@Override
		public String getDatabaseName() {
			final String packageName = PackageUtils.getPackageName(mContext);
			return String.format(Locale.getDefault(), "%s.db", packageName);
		}

		@Override
		public int getDatabaseVersion() {
			final int versionCode = PackageUtils.getVersionCode(mContext);
			return versionCode > 0 ? versionCode : 1;
		}

		@Override
		public CursorFactory getCursorFactory() {
			return null;
		}

		@Override
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public DatabaseErrorHandler getErrorHandler() {
			return null;
		}

	}
}