package com.xtreme.rest.provider;

import com.xtreme.rest.provider.Column.Type;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLDataset implements Dataset {

	protected static interface Columns {
		public static final Column _ID = new Column("_id", Type._ID);
	}
	
	public abstract void onCreate(final SQLiteDatabase db);
	public abstract void onDrop(final SQLiteDatabase db);
	
	private SQLiteDatabase mDatabase;

	
	protected SQLiteDatabase getDatabase() {
		return mDatabase;
	}
	
	void setDatabase(final SQLiteDatabase db) {
		mDatabase = db;
	}
	
	public String getName() {
		return getClass().getSimpleName();
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
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		onDrop(db);
		onCreate(db);
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
	public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		onDrop(db);
		onCreate(db);
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		return getDatabase().query(getName(), projection, selection, selectionArgs, null, null, sortOrder);
	}
	
}
