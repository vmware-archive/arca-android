package com.xtreme.rest.providers;

import android.annotation.TargetApi;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class RestSQLCursor extends SQLiteCursor {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public RestSQLCursor(final SQLiteCursorDriver driver, final String editTable, final SQLiteQuery query) {
		super(driver, editTable, query);
		Log.v("rest", query.toString());
	}

	@SuppressWarnings("deprecation")
	public RestSQLCursor(final SQLiteDatabase db, final SQLiteCursorDriver driver, final String editTable, final SQLiteQuery query) {
		super(db, driver, editTable, query);
		Log.v("rest", query.toString());
	}
	
	private Bundle mExtras = Bundle.EMPTY;
	
	@Override
    public Bundle respond(final Bundle extras) {
		mExtras = extras;
		return mExtras;
    }

    @Override
    public Bundle getExtras() {
    	return mExtras;
    }
    
}
