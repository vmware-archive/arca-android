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
 * This class is an extension of the {@link SQLiteOpenHelper} that directly 
 * forwards events to its {@link Dataset}s.
 */
public class SQLHelper extends SQLiteOpenHelper {
	
	public static SQLHelper create(final Context context, final Configuration config, final Collection<Dataset> datasets) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return new SQLHelper(context, config.getDatabaseName(), config.getCursorFactory(), config.getDatabaseVersion(), datasets);
		} else {
			return new SQLHelper(context, config.getDatabaseName(), config.getCursorFactory(), config.getDatabaseVersion(), config.getErrorHandler(), datasets);
		}
	}
	
	private final Collection<Dataset> mDatasets;
	
	public SQLHelper(final Context context, final String name, final CursorFactory factory, final int version, final Collection<Dataset> datasets) {
		super(context, name, factory, version);
		mDatasets = datasets;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SQLHelper(final Context context, final String name, final CursorFactory factory, final int version, final DatabaseErrorHandler errorHandler, final Collection<Dataset> datasets) {
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
		for (final Dataset dataset : mDatasets) {
			if (dataset instanceof SQLTable) {
				createDataset(db, (SQLTable) dataset);
			}
		}
	}
	
	private void createOtherDatasets(final SQLiteDatabase db) {
		for (final Dataset dataset : mDatasets) {
			if (!(dataset instanceof SQLTable) && (dataset instanceof SQLDataset)) { 
				createDataset(db, (SQLDataset) dataset);
			}
		}
	}

	private void upgradeTables(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final Dataset dataset : mDatasets) {
			if (dataset instanceof SQLTable) { 
				upgradeDataset(db, oldVersion, newVersion, (SQLTable) dataset);
			}
		}
	}
	
	private void upgradeOtherDatasets(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final Dataset dataset : mDatasets) {
			if (!(dataset instanceof SQLTable) && (dataset instanceof SQLDataset)) { 
				upgradeDataset(db, oldVersion, newVersion, (SQLDataset) dataset);
			}
		}
	}

	private void downgradeTables(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final Dataset dataset : mDatasets) {
			if (dataset instanceof SQLTable) { 
				downgradeDataset(db, oldVersion, newVersion, (SQLTable) dataset);
			}
		}
	}

	private void downgradeOtherDatasets(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		for (final Dataset dataset : mDatasets) {
			if (!(dataset instanceof SQLTable) && (dataset instanceof SQLDataset)) { 
				downgradeDataset(db, oldVersion, newVersion, (SQLDataset) dataset);
			}
		}
	}
	
	private static void createDataset(final SQLiteDatabase db, final SQLDataset dataset) {
		dataset.setDatabase(db);
		dataset.onCreate();
	}
	
	private static void upgradeDataset(final SQLiteDatabase db, final int oldVersion, final int newVersion, final SQLDataset dataset) {
		dataset.setDatabase(db);
		dataset.onUpgrade(oldVersion, newVersion);
	}
	
	private static void downgradeDataset(final SQLiteDatabase db, final int oldVersion, final int newVersion, final SQLDataset dataset) {
		dataset.setDatabase(db);
		dataset.onDowngrade(oldVersion, newVersion);
	}
	
}
