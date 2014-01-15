package com.xtreme.rest.provider;

import java.util.Map;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * An object representation of a database table that simplifies table 
 * management by abstracting common SQL operations.
 */
public abstract class SQLTable extends SQLDataset {

	/**
	 * This method is called automatically by the {@link DatabaseHelper} when 
	 * it is being initialized. This call will automatically create the 
	 * database table represented by the values returned from the 
	 * abstract classes. 
	 * 
	 * This method can be overwritten for custom table creation.
	 */
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final Map<String, String> mapping = onCreateColumnMapping();
		if (mapping == null)
			throw new IllegalStateException("Please override onCreateColumnMapping and supply a valid SQLite mapping between column name and type.");

		final String query = SQLUtils.generateCreateTableStatement(getName(), mapping, onCreateUniqueConstraint());
		db.execSQL(query);
	}
	
	@Override
	public void onDrop(final SQLiteDatabase db) {
		final String query = SQLUtils.generateDropTableStatement(getName());
		db.execSQL(query);
	}

	/**
	 * This method must return a mapping between each column name and SQL 
	 * data type that will be applied on table creation.
	 * 
	 * @return
	 */
	protected Map<String, String> onCreateColumnMapping() {
		return null;
	}
	
	/**
	 * This method must return a string that specifies a valid SQL unique 
	 * constraint, which will be applied to the table on creation. Or null 
	 * for no unique constraint.
	 * 
	 * @return
	 */
	protected String onCreateUniqueConstraint() {
		return null;
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		return getDatabase().update(getName(), values, selection, selectionArgs);
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		return getDatabase().delete(getName(), selection, selectionArgs);
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		final long id = getDatabase().insert(getName(), null, values);
		return ContentUris.withAppendedId(uri, id);
	}
	
	/**
	 * This method inserts all {@link ContentValues} into the underlying
	 * database within a transaction.
	 * 
	 * @return numInserted
	 */
	@Override
	public int bulkInsert(final Uri uri, final ContentValues[] values) {
		final SQLiteDatabase database = getDatabase();
		database.beginTransaction();
		try {
			final int numInserted = insert(database, getName(), values);
			database.setTransactionSuccessful();
			return numInserted;
		} finally {
			database.endTransaction();
		}
	}

	private static int insert(final SQLiteDatabase database, final String name, final ContentValues[] values) {
		int numInserted = 0;
		for (final ContentValues value : values) {
			if (database.insert(name, null, value) > -1) {
				numInserted++;
			}
		}
		return numInserted;
	}
}
