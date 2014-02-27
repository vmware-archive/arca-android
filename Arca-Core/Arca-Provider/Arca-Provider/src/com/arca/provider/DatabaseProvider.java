package com.arca.provider;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.arca.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;

public abstract class DatabaseProvider extends DatasetProvider {

	private volatile SQLiteDatabase mDatabase;

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
	
	protected SQLiteDatabase createDatabase() {
		final DatabaseConfiguration configuration = onCreateDatabaseConfiguration();
		final DatabaseHelper helper = DatabaseHelper.create(getContext(), configuration, getSQLiteDatasets());
		return helper.getWritableDatabase();
	}
	
	public DatabaseConfiguration onCreateDatabaseConfiguration() {
		return new DefaultDatabaseConfiguration(getContext());
	}
	
	private Collection<SQLiteDataset> getSQLiteDatasets() {
		final Collection<Dataset> datasets = getDatasets(); 
		return CollectionUtils.filter(datasets, SQLiteDataset.class);
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
