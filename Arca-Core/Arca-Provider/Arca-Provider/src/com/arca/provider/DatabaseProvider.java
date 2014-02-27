package com.arca.provider;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.arca.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;

/**
 * The class adds {@link SQLiteDatabase} support on top of the
 * {@link DatasetProvider} implementation.
 */
public abstract class DatabaseProvider extends DatasetProvider {

	private volatile SQLiteDatabase mDatabase;
	
	/**
	 * Creates a {@link DatabaseConfiguration} object that can be overridden to
	 * customize the database name, version and other params.
	 * 
	 * The default implementation for this method creates a database with the
	 * packageName and versionCode from your AndroidManifest.xml.
	 * 
	 * @return A {@link DatabaseConfiguration}.
	 */
	public DatabaseConfiguration onCreateDatabaseConfiguration() {
		return new DefaultDatabaseConfiguration(getContext());
	}

	/**
	 * Creates a {@link SQLiteDatabase} object.
	 * 
	 * Override this method as a last resort if you want strict control over
	 * which {@link SQLiteDatabase} gets used by this {@link ContentProvider}.
	 * 
	 * Otherwise override {@link #onCreateDatabaseConfiguration()}
	 * for flexibility in defining database name and version.
	 * 
	 * @return A read/write enabled {@link SQLiteDatabase}.
	 */
	protected SQLiteDatabase createDatabase() {
		final DatabaseConfiguration configuration = onCreateDatabaseConfiguration();
		final DatabaseHelper helper = DatabaseHelper.create(getContext(), configuration, getSQLiteDatasets());
		return helper.getWritableDatabase();
	}
	
	private Collection<SQLiteDataset> getSQLiteDatasets() {
		final Collection<Dataset> datasets = getDatasets(); 
		return CollectionUtils.filter(datasets, SQLiteDataset.class);
	}
	
	/**
	 * Provides access to the underlying {@link SQLiteDatabase} object, calling
	 * {@link #createDatabase()} if it doesn't already exists.
	 * 
	 * @return A read/write enabled {@link SQLiteDatabase}.
	 */
	protected SQLiteDatabase getDatabase() {
		synchronized (this) {
			if (mDatabase == null) {
				mDatabase = createDatabase();
			}	
		}
		return mDatabase;
	}

	protected void closeDatabase() {
		synchronized (this) {
			if (mDatabase != null) {
				mDatabase.close();
				mDatabase = null;
			}
		}
	}
	
	@Override
	protected Dataset getDatasetOrThrowException(final Uri uri) {
		final Dataset dataset = super.getDatasetOrThrowException(uri);
		if (dataset instanceof SQLiteDataset) {
			final SQLiteDatabase database = getDatabase();
			((SQLiteDataset) dataset).setDatabase(database);
		}
		return dataset;
	}

	@Override
	public ContentProviderResult[] applyBatch(final ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
		final SQLiteDatabase database = getDatabase();
		database.beginTransaction();
		try {
			final ContentProviderResult[] results = super.applyBatch(operations);
			database.setTransactionSuccessful();
			return results;
		} finally {
			database.endTransaction();
		}
	}
}
