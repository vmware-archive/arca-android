package com.xtreme.rest.providers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLDataset implements Dataset {

	public abstract String getName();
	public abstract void onCreate();
	public abstract void onDrop();
	
	
	private SQLiteDatabase mDatabase;

	protected SQLiteDatabase getDatabase() {
		return mDatabase;
	}
	
	public void setDatabase(final SQLiteDatabase db) {
		mDatabase = db;
	}
	
	/**
	 * This method should be overwritten for migrations support.<br>
	 * <br>
	 * This method is called when a database upgrade is detected. The default 
	 * implementation is to drop the existing {@link Dataset} and create a new one.
	 * 
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onUpgrade(final int oldVersion, final int newVersion) {
		onDrop();
		onCreate();
	}

	/**
	 * /** This method should be overwritten for migrations support.<br>
	 * <br>
	 * This method is called when a database downgrade is detected. The default 
	 * implementation is to drop the existing {@link Dataset} and create a new one.
	 * 
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onDowngrade(final int oldVersion, final int newVersion) {
		onDrop();
		onCreate();
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		return getDatabase().query(getName(), projection, selection, selectionArgs, null, null, sortOrder);
	}
	
}
