package com.xtreme.rest.providers;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;

import com.xtreme.rest.providers.Configuration.DefaultConfiguration;

/**
 * The class adds {@link SQLiteDatabase} support on top of the
 * {@link DatasetProvider} implementation.
 */
public abstract class RestContentProvider extends DatasetProvider {

	private static final Object LOCK = new Object();

	private SQLiteDatabase mDatabase;

	/**
	 * Creates a {@link Configuration} object that can be overridden to
	 * customize the database name, version and other params.
	 * 
	 * The default implementation for this method creates a database with the
	 * packageName and versionCode from your AndroidManifest.xml.
	 * 
	 * @return A {@link Configuration}.
	 */
	public Configuration onCreateConfiguration() {
		return new DefaultConfiguration(getContext());
	}

	/**
	 * Creates a {@link SQLiteDatabase} object.
	 * 
	 * Override this method as a last resort if you want strict control over
	 * which {@link SQLiteDatabase} gets used by this {@link ContentProvider}.
	 * 
	 * Otherwise override {@link RestContentProvider#onCreateConfiguration()}
	 * for flexibility in defining database name and version.
	 * 
	 * @return A read/write enabled {@link SQLiteDatabase}.
	 */
	protected SQLiteDatabase createDatabase() {
		final Configuration configuration = onCreateConfiguration();
		final SQLHelper helper = SQLHelper.create(getContext(), configuration, getDatasets());
		return helper.getWritableDatabase();
	}

	/**
	 * Provides access to the underlying {@link SQLiteDatabase} object, calling
	 * {@link RestContentProvider#createDatabase()} if it doesn't already
	 * exists.
	 * 
	 * @return A read/write enabled {@link SQLiteDatabase}.
	 */
	protected SQLiteDatabase getDatabase() {
		synchronized (LOCK) {
			if (mDatabase == null) {
				mDatabase = createDatabase();
			}
		}
		return mDatabase;
	}

	/**
	 * Closes the {@link SQLiteDatabase} object and destroys the reference.
	 * 
	 * @return
	 */
	protected void destroyDatabase() {
		synchronized (LOCK) {
			if (mDatabase != null) {
				mDatabase.close();
				mDatabase = null;
			}
		}
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
