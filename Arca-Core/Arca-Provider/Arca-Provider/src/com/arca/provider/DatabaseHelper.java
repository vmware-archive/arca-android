package com.arca.provider;

import java.util.Collection;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * This class is an extension of the {@link SQLiteOpenHelper} that directly 
 * forwards events to its {@link SQLiteDataset}s.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static DatabaseHelper create(final Context context, final DatabaseConfiguration config, final Collection<SQLiteDataset> datasets) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
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
