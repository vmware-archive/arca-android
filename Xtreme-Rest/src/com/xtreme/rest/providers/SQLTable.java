package com.xtreme.rest.providers;

import java.util.Map;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * An object representation of a database table that simplifies table management by abstracting common SQL operations.
 */
public abstract class SQLTable extends Dataset {

	private static final String EXCEPTION_MESSAGE = "Please override onCreateColumnMapping and supply a valid SQLite mapping between column name and type.";
	
	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s ( %s%s );";
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS %s;";

	/**
	 * This method is called automatically by the {@link Database} when it is initializing. This call will automatically create the database table represented by the values returned from the abstract classes. This method
	 * can be overwritten for custom table creation.
	 * 
	 * @param db
	 */
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final Map<String, String> mapping = onCreateColumnMapping();
		if (mapping == null)
			throw new IllegalStateException(EXCEPTION_MESSAGE);

		final String query = generateQuery(mapping);
		db.execSQL(query);
	}

	/**
	 * This method must return a mapping providing descriptions of the columns that will be created on table creation.<br>
	 * 
	 * @return
	 */
	protected Map<String, String> onCreateColumnMapping() {
		return null;
	}
	
	/**
	 * This method must return a string that specifies a valid SQL unique constraint, which will be applied to the table on creation. Or null for nothing unique.<br>
	 * 
	 * @return
	 */
	protected String onCreateUniqueConstraint() {
		return null;
	}

	/**
	 * This method truncates all content from this table.
	 * 
	 * @param db
	 */
	public void truncate(final SQLiteDatabase db) {
		delete(db, null, null);
	}

	/**
	 * This method drops this table from the database.
	 * 
	 * @param db
	 */
	@Override
	public void drop(final SQLiteDatabase db) {
		final String query = String.format(DROP_TABLE, getName());
		db.execSQL(query);
	}
	
	@Override
	public int update(final SQLiteDatabase database, final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		return database.update(getName(), values, selection, selectionArgs);
	}

	@Override
	public int update(final SQLiteDatabase database, final ContentValues values, final String selection, final String[] selectionArgs) {
		return update(database, null, values, selection, selectionArgs);
	}

	@Override
	public int delete(final SQLiteDatabase database, final Uri uri, final String selection, final String[] selectionArgs) {
		return database.delete(getName(), selection, selectionArgs);
	}

	@Override
	public int delete(final SQLiteDatabase database, final String selection, final String[] selectionArgs) {
		return delete(database, null, selection, selectionArgs);
	}

	@Override
	public long insert(final SQLiteDatabase database, final Uri uri, final ContentValues values) {
		return database.insert(getName(), null, values);
	}
	
	@Override
	public long insert(final SQLiteDatabase database, final ContentValues values) {
		return insert(database, null, values);
	}
	
	@Override
	public int bulkInsert(final SQLiteDatabase database, final ContentValues[] values) {
		return bulkInsert(database, null, values);
	}

	@Override
	public int bulkInsert(final SQLiteDatabase database, final Uri uri, final ContentValues[] values) {
		int numInserted = 0;
		database.beginTransaction();
		try {
			for (final ContentValues value : values) {
				if (insert(database, uri, value) > -1)
					numInserted++;
			}
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		return numInserted;
	}
	
	
	// ======================================
	
	
	private String generateQuery(final Map<String, String> mapping) {
		return String.format(CREATE_TABLE, getName(), generateColumnString(mapping), generateUniqueString());
	}
	
	private static String generateColumnString(final Map<String, String> mapping) {
		final StringBuilder builder = new StringBuilder();
		for (final String key : mapping.keySet()) {
			builder.append(String.format("%s %s,", key, mapping.get(key)));
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() -1);
		}
		return builder.toString();
	}
	
	private String generateUniqueString() {
		final String uniqueString = onCreateUniqueConstraint();
		return uniqueString != null ? String.format(", %s", uniqueString) : "";
	}
}
