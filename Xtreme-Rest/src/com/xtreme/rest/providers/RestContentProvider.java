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
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;
import android.os.Build;

import com.xtreme.rest.loader.ContentLoader;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.utils.CursorUtils;

/**
 * The base {@link ContentProvider} class to be used with {@link Dataset}s, {@link ContentLoader}s, and {@link ContentRequest}s.
 * This provides the initialization of all necessary components and where all the {@link ContentRequest}s come through. Queries
 * for content occur through here, followed by a call to {@link DatasetValidator#validate(Uri, Cursor)} which then may or may
 * not need to fetch new data via a call to {@link DatasetValidator#fetchData(Context, Uri, Cursor)}.
 */
public abstract class RestContentProvider extends ContentProvider {

	private static final Object LOCK = new Object();
	private static SQLiteDatabase sDatabase;

	private DatasetMatcher mMatcher = new DatasetMatcher();

	/**
	 * Provides access to the {@link SQLiteDatabase} directly. Note that this method will create the database if has not been created yet.
	 * 
	 * @return A read/write enabled {@link SQLiteDatabase}.
	 */
	protected SQLiteDatabase getDatabase() {
		if (sDatabase == null) {
			Database.throwExceptionIfNotInitialized();
			createDatabase();
		}
		return sDatabase;
	}

	/**
	 * Initializes the {@link SQLiteDatabase}, {@link CursorFactory}, and the collection of {@link Dataset}s. It is recommended that you keep this logic as is by always calling super.
	 */
	protected void createDatabase() {
		synchronized (LOCK) {
			if (sDatabase == null) {
				final Context context = getContext();
				final CursorFactory cursorFactory = getCursorFactory();
				final Collection<Dataset> datasets = mMatcher.getDatasets();
				final Database database = new Database(context, cursorFactory, datasets);
				sDatabase = database.getWritableDatabase();
			}
		}
	}

	/**
	 * Closes the {@link SQLiteDatabase} object and destroys the reference.
	 */
	protected void destroyDatabase() {
		synchronized (LOCK) {
			if (sDatabase != null) {
				sDatabase.close();
				sDatabase = null;
			}
		}
	}

	/**
	 * Register a {@link Dataset} with this {@link ContentProvider} on the specified path.
	 * 
	 * @param authority The {@link ContentProvider}'s authority
	 * @param path The path associated with this {@link Dataset}
	 * @param datasetClass The {@link Dataset}'s class
	 */
	protected void registerDataset(final String authority, final String path, final Class<? extends Dataset> datasetClass) {
		registerDataset(authority, path, datasetClass, null);
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

	private Dataset getDatasetOrThrowException(final Uri uri) {
		final Dataset dataset = mMatcher.getDataset(uri);
		if (dataset == null) {
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return dataset;
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
		final DatasetValidator validator = mMatcher.getValidator(uri);
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

	private static CursorFactory getCursorFactory() {
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
