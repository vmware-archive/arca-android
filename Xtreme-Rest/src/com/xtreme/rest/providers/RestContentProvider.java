package com.xtreme.rest.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xtreme.rest.loader.ContentLoader;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.providers.DatasetMatcher.DefaultMatcher;
import com.xtreme.rest.utils.CursorUtils;

/**
 * The base {@link ContentProvider} class to be used with {@link Dataset}s, {@link ContentLoader}s, 
 * and {@link ContentRequest}s. This class initializes a {@link DatasetMatcher} that acts as a 
 * router between a {@link ContentRequest} and a {@link Dataset}. Queries for content pass through 
 * a {@link DatasetValidator#validate(Uri, Cursor)} method which may or may not proceed to fetch new 
 * data via a call to {@link DatasetValidator#fetchData(Context, Uri, Cursor)}.
 */
public abstract class RestContentProvider extends ContentProvider {

	private static final Object LOCK = new Object();
	
	private SQLiteDatabase mDatabase;
	private DatasetMatcher mMatcher = new DefaultMatcher();
	
	/**
	 * Provides access to the {@link SQLiteDatabase} directly. 
	 * Note that this method will create the database if has not been created yet.
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
	 */
	protected void destroyDatabase() {
		synchronized (LOCK) {
			if (mDatabase != null) {
				mDatabase.close();
				mDatabase = null;
			}
		}
	}
	
	/**
	 * Creates a {@link Database.Builder} object that can be overridden to customize the database 
	 * name, version and other params. The default implementation for this method creates a database with 
	 * the packageName and versionCode from your AndroidManifest.xml.
	 * 
	 * @return A {@link Database.Builder}.
	 */
	protected Database.Builder onCreateDatabaseBuilder() {
		final Context context = getContext();
		final Collection<Dataset> datasets = getDatasets();
		return new Database.Builder(context, datasets);
	}
	
	/**
	 * Creates a {@link SQLiteDatabase} object. Override this method as a last resort if you want 
	 * strict control over which {@link SQLiteDatabase} gets used by this {@link ContentProvider}. 
	 * Otherwise override {@link RestContentProvider#onCreateDatabaseBuilder()} for flexibility 
	 * in defining database name and version.
	 * 
	 * @return A read/write enabled {@link SQLiteDatabase}.
	 */
	protected SQLiteDatabase createDatabase() {
		final Database.Builder builder = onCreateDatabaseBuilder();
		final Database database = builder.create();
		return database.getWritableDatabase();
	}
	
	/**
	 * Provides access to the collection of {@link Dataset}s that have been registered with this provider.
	 * 
	 * @return A collection of {@link Dataset}s.
	 */
	protected Collection<Dataset> getDatasets() {
		return mMatcher.getDatasets();
	}
	
	/**
	 * Register a {@link Dataset} with this {@link ContentProvider} on the specified path.
	 * 
	 * @param authority The {@link ContentProvider}'s authority
	 * @param path The path associated with this {@link Dataset}
	 * @param datasetClass The {@link Dataset}'s class
	 */
	protected void registerDataset(final String authority, final String path, final Class<? extends Dataset> datasetClass) {
		mMatcher.register(authority, path, datasetClass);
	}

	/**
	 * Register a {@link Dataset} with this {@link ContentProvider} on the specified path.
	 * 
	 * @param authority The {@link ContentProvider}'s authority
	 * @param path The path associated with this {@link Dataset}
	 * @param datasetClass The {@link Dataset}'s class
	 * @param validatorClass The {@link DatasetValidator} class associated with this {@link Dataset}
	 */
	protected void registerDataset(final String authority, final String path, final Class<? extends Dataset> datasetClass, final Class<? extends DatasetValidator> validatorClass) {
		mMatcher.register(authority, path, datasetClass, validatorClass);
	}

	// ======================================================

	@Override
	public String getType(final Uri uri) {
		final String packageName = getContext().getPackageName();
		final String datasetName = getDatasetOrThrowException(uri).getName();
		final String type = "vnd.android.cursor.dir" + "/" + packageName + "." + datasetName;
		return type.toLowerCase(Locale.getDefault());
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final int numRowsDeleted = dataset.delete(getDatabase(), uri, selection, selectionArgs);
		return numRowsDeleted;
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final long id = dataset.insert(getDatabase(), uri, values);
		return ContentUris.withAppendedId(uri, id);
	}

	@Override
	public int bulkInsert(final Uri uri, final ContentValues[] values) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final int numRowsInserted = dataset.bulkInsert(getDatabase(), uri, values);
		return numRowsInserted;
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final int numRowsAffected = dataset.update(getDatabase(), uri, values, selection, selectionArgs);
		return numRowsAffected;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		final Dataset dataset = getDatasetOrThrowException(uri);
		final DatasetValidator validator = mMatcher.matchValidator(uri);
		final Cursor cursor = dataset.query(getDatabase(), uri, projection, selection, selectionArgs, sortOrder);
		CursorUtils.updateCursorWithExtras(getContext(), uri, cursor, validator);
		return cursor;
	}

	@Override
	public ContentProviderResult[] applyBatch(final ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
		final SQLiteDatabase db = getDatabase();
		db.beginTransaction();
		try {
			final ContentProviderResult[] results = super.applyBatch(operations);
			db.setTransactionSuccessful();
			return results;
		} finally {
			db.endTransaction();
		}
	}
	
	// ======================================================
	
	private Dataset getDatasetOrThrowException(final Uri uri) {
		final Dataset dataset = mMatcher.matchDataset(uri);
		if (dataset == null) {
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return dataset;
	}
}
