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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.Collection;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static DatabaseHelper create(final Context context, final DatabaseConfiguration config, final Collection<SQLiteDataset> datasets) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			return new DatabaseHelper(context, config.getDatabaseName(), config.getCursorFactory(), config.getDatabaseVersion(), datasets);
		} else {
			return new DatabaseHelper(context, config.getDatabaseName(), config.getCursorFactory(), config.getDatabaseVersion(), config.getErrorHandler(), datasets);
		}
	}

	private final Collection<SQLiteDataset> mDatasets;

	public DatabaseHelper(final Context context, final String name, final CursorFactory factory, final int version, final Collection<SQLiteDataset> datasets) {
		super(context, name, factory, version);
		mDatasets = datasets;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public DatabaseHelper(final Context context, final String name, final CursorFactory factory, final int version, final DatabaseErrorHandler errorHandler, final Collection<SQLiteDataset> datasets) {
		super(context, name, factory, version, errorHandler);
		mDatasets = datasets;
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		createTables(db);
		createOtherDatasets(db);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		upgradeTables(db, oldVersion, newVersion);
		upgradeOtherDatasets(db, oldVersion, newVersion);
	}

	@Override
	public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		downgradeTables(db, oldVersion, newVersion);
		downgradeOtherDatasets(db, oldVersion, newVersion);
	}

	private void createTables(final SQLiteDatabase db) {
		for (final SQLiteDataset dataset : mDatasets) {
			if (dataset instanceof SQLiteTable) {
				dataset.onCreate(db);
			}
		}
	}

	private void createOtherDatasets(final SQLiteDatabase db) {
		for (final SQLiteDataset dataset : mDatasets) {
			if (!(dataset instanceof SQLiteTable)) {
				dataset.onCreate(db);
			}
		}
	}

	private void upgradeTables(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final SQLiteDataset dataset : mDatasets) {
			if (dataset instanceof SQLiteTable) {
				dataset.onUpgrade(db, oldVersion, newVersion);
			}
		}
	}

	private void upgradeOtherDatasets(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final SQLiteDataset dataset : mDatasets) {
			if (!(dataset instanceof SQLiteTable)) {
				dataset.onUpgrade(db, oldVersion, newVersion);
			}
		}
	}

	private void downgradeTables(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final SQLiteDataset dataset : mDatasets) {
			if (dataset instanceof SQLiteTable) {
				dataset.onDowngrade(db, oldVersion, newVersion);
			}
		}
	}

	private void downgradeOtherDatasets(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final SQLiteDataset dataset : mDatasets) {
			if (!(dataset instanceof SQLiteTable)) {
				dataset.onDowngrade(db, oldVersion, newVersion);
			}
		}
	}

}
