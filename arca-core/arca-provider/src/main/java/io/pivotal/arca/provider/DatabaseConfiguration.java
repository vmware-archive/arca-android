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