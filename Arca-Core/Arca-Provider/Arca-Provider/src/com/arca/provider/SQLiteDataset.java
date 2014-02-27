package com.arca.provider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.arca.provider.Column.Type;

public abstract class SQLiteDataset implements Dataset {

	protected static interface Columns {
		public static final Column _ID = Type._ID.newColumn("_id");
		public static final Column _STATE = Type._STATE.newColumn("_state");
	}
	
	public abstract void onCreate(final SQLiteDatabase db);
	public abstract void onDrop(final SQLiteDatabase db);
	
	private SQLiteDatabase mDatabase;

	
	protected SQLiteDatabase getDatabase() {
		return mDatabase;
	}
	
	protected void setDatabase(final SQLiteDatabase db) {
		mDatabase = db;
	}
	
	public String getName() {
		return getClass().getSimpleName();
	}

	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		onDrop(db);
		onCreate(db);
	}

	public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		onDrop(db);
		onCreate(db);
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			return database.query(getName(), projection, selection, selectionArgs, null, null, sortOrder);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}
	
}
