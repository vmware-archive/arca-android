package com.xtreme.rest.providers;

import java.util.Collection;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * This class is an extension of the {@link SQLiteOpenHelper} that directly forwards events to database {@link Dataset}.
 */
public class Database extends SQLiteOpenHelper {
	
	private static final String NOT_INITIALIZED = "Database has not been initialized. Please do this in your applications onCreate method.";
	
	private static String sDatabaseName;
	private static int sDatabaseVersion;
	
	public static synchronized void init(final String name, final int version) {
		sDatabaseName = name;
		sDatabaseVersion = version;
	}
	
	public static synchronized boolean isInitialized() {
		return sDatabaseName != null && sDatabaseVersion > 0;
	}
	
	public static void throwExceptionIfNotInitialized() {
		if (!isInitialized()) 
			throw new IllegalStateException(NOT_INITIALIZED);
	}
	
	private final Collection<Dataset> mDatasets;

	/**
	 * @param context
	 * @param factory
	 * @param datasets
	 *            A list of all {@link Dataset} instances.
	 */
	public Database(final Context context, final CursorFactory factory, final Collection<Dataset> datasets) {
		super(context, sDatabaseName, factory, sDatabaseVersion);
		if (datasets == null)
			throw new IllegalArgumentException("The list of datasets cannot be null!");
		mDatasets = datasets;
	}

	/**
	 * 
	 * @param context
	 * @param factory
	 * @param datasets
	 *            A list of all {@link Dataset} instances.
	 * @param errorHandler
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public Database(final Context context, final CursorFactory factory, final Collection<Dataset> datasets, final DatabaseErrorHandler errorHandler) {
		super(context, sDatabaseName, factory, sDatabaseVersion, errorHandler);
		if (datasets == null)
			throw new IllegalArgumentException("The list of datasets cannot be null!");
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
		for (final Dataset dataset : mDatasets) {
			if (dataset instanceof SQLTable) { 
				dataset.onCreate(db);
			}
		}
	}
	
	private void createOtherDatasets(final SQLiteDatabase db) {
		for (final Dataset dataset : mDatasets) {
			if (!(dataset instanceof SQLTable)) { 
				dataset.onCreate(db);
			}
		}
	}

	private void upgradeTables(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final Dataset dataset : mDatasets) {
			if (dataset instanceof SQLTable) { 
				dataset.onUpgrade(db, oldVersion, newVersion);
			}
		}
	}
	
	private void upgradeOtherDatasets(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final Dataset dataset : mDatasets) {
			if (!(dataset instanceof SQLTable)) { 
				dataset.onUpgrade(db, oldVersion, newVersion);
			}
		}
	}

	private void downgradeTables(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final Dataset dataset : mDatasets) {
			if (dataset instanceof SQLTable) { 
				dataset.onDowngrade(db, oldVersion, newVersion);
			}
		}
	}

	private void downgradeOtherDatasets(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final Dataset dataset : mDatasets) {
			if (!(dataset instanceof SQLTable)) { 
				dataset.onDowngrade(db, oldVersion, newVersion);
			}
		}
	}

	public void truncate() {
		final SQLiteDatabase db = getWritableDatabase();
		for (final Dataset dataset : mDatasets) {
			if (dataset instanceof SQLTable) {
				SQLTable table = (SQLTable) dataset;
				table.truncate(db);
			}
		}
	}

	public void reset() {
		for (final Dataset dataset : mDatasets) {
			final SQLiteDatabase db = getWritableDatabase();
			dataset.drop(db);
			dataset.onCreate(db);
		}
	}
}
