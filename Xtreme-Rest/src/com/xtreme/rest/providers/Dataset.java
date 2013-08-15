package com.xtreme.rest.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class Dataset {

	protected Dataset() {}
	
	/**
	 * This method must return the unique name of the dataset.
	 * 
	 * @return
	 */
	public abstract String getName();
	
	public abstract void onCreate(SQLiteDatabase db);

	/**
	 * This method should be overwritten for migrations support.<br>
	 * <br>
	 * This method is called when a database upgrade is detected. The default implementation is to drop the existing dataset and create a new dataset.
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		drop(db);
		onCreate(db);
	}

	/**
	 * /** This method should be overwritten for migrations support.<br>
	 * <br>
	 * This method is called when a database downgrade is detected. The default implementation is to drop the existing dataset and create a new dataset.
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		drop(db);
		onCreate(db);
	}

	public Cursor query(final SQLiteDatabase database, final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		return database.query(getName(), projection, selection, selectionArgs, null, null, sortOrder);
	}

	public abstract int delete(SQLiteDatabase database, Uri uri, String selection, String[] selectionArgs);

	public abstract int delete(SQLiteDatabase database, String selection, String[] selectionArgs);

	public abstract int update(SQLiteDatabase database, Uri uri, ContentValues values, String selection, String[] selectionArgs);

	public abstract int update(SQLiteDatabase database, ContentValues values, String selection, String[] selectionArgs);

	public abstract int bulkInsert(SQLiteDatabase database, Uri uri, ContentValues[] values);

	public abstract int bulkInsert(SQLiteDatabase database, ContentValues[] values);

	public abstract long insert(SQLiteDatabase database, Uri uri, ContentValues values);

	public abstract long insert(SQLiteDatabase database, ContentValues values);
	
	public abstract void drop(SQLiteDatabase db);
}
