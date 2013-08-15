package com.xtreme.rest.providers;

import java.util.Collection;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.os.Build;

import com.xtreme.rest.utils.PackageUtils;

/**
 * This class is an extension of the {@link SQLiteOpenHelper} that directly forwards events to its {@link Dataset}s.
 */
public class Database extends SQLiteOpenHelper {
	
	private final Collection<Dataset> mDatasets;

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param datasets
	 *            A list of all {@link Dataset} instances.
	 */
	public Database(final Context context, final String name, final CursorFactory factory, final int version, final Collection<Dataset> datasets) {
		super(context, name, factory, version);
		if (datasets == null)
			throw new IllegalArgumentException("The list of datasets cannot be null!");
		mDatasets = datasets;
	}

	/**
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 * @param datasets
	 *            A list of all {@link Dataset} instances.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public Database(final Context context, final String name, final CursorFactory factory, final int version, final DatabaseErrorHandler errorHandler, final Collection<Dataset> datasets) {
		super(context, name, factory, version, errorHandler);
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
	
	public static class Builder {

		private final Context mContext;

		private String mDatabaseName;
		private int mDatabaseVersion;
		private CursorFactory mCursorFactory;
		private Collection<Dataset> mDatasets;
		
		public Builder(final Context context, final Collection<Dataset> datasets) {
			mDatasets = datasets;
			mContext = context;
		}

		public Builder setDatabaseName(final String databaseName) {
			mDatabaseName = databaseName;
			return this;
		}
		
		public Builder setDatabaseVersion(final int databaseVersion) {
			mDatabaseVersion = databaseVersion;
			return this;
		}
		
		public Builder setCursorFactory(final CursorFactory factory) {
			mCursorFactory = factory;
			return this;
		}
		
		public Database create() {
			
			if (mDatabaseName == null) {
				mDatabaseName = createDefaultDatabaseName(mContext);
			}
			
			if (mDatabaseVersion <= 0) {
				mDatabaseVersion = createDefaultDatabaseVersion(mContext);
			}
			
			if (mCursorFactory == null) {
				mCursorFactory = createDefaultCursorFactory();
			}
			
			return new Database(mContext, mDatabaseName, mCursorFactory, mDatabaseVersion, mDatasets);
		}
		
		// ======================================================
		
		public static String createDefaultDatabaseName(final Context context) {
			final String packageName = PackageUtils.getPackageName(context);
			return String.format(Locale.getDefault(), "%s.db", packageName);
		}

		public static int createDefaultDatabaseVersion(final Context context) {
			final int versionCode = PackageUtils.getVersionCode(context);
			return versionCode > 0 ? versionCode : 1;
		}

		public static CursorFactory createDefaultCursorFactory() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				return new RestCursorFactory();
			} else {
				return new RestSupportCursorFactory();
			}
		}

		private static final class RestSupportCursorFactory implements CursorFactory {
			@Override
			public Cursor newCursor(final SQLiteDatabase db, final SQLiteCursorDriver masterQuery, final String editTable, final SQLiteQuery query) {
				return new RestSQLCursor(db, masterQuery, editTable, query);
			}
		}

		private static final class RestCursorFactory implements CursorFactory {
			@Override
			public Cursor newCursor(final SQLiteDatabase db, final SQLiteCursorDriver masterQuery, final String editTable, final SQLiteQuery query) {
				return new RestSQLCursor(masterQuery, editTable, query);
			}
		}
	}
}
