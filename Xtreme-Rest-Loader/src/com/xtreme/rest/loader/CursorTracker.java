package com.xtreme.rest.loader;

import java.util.HashSet;
import java.util.Set;

import android.database.ContentObserver;
import android.database.Cursor;

final class CursorTracker {
	
	private final Set<Cursor> mRegistered = new HashSet<Cursor>();
	
	private Cursor mInvalidCursor;
    private Cursor mValidCursor;

	public Cursor getCursor() {
		return mValidCursor;
	}

	void registerObserver(final Cursor cursor, final ContentObserver observer) {
		if (cursor != null && !mRegistered.contains(cursor)) {
    		cursor.registerContentObserver(observer);
    		mRegistered.add(cursor);
    	}
	}
	
	void trackValidCursor(final Cursor cursor) {
		if (mInvalidCursor != cursor) {
			closeCursor(mInvalidCursor);
		}
			
		if (mValidCursor != cursor) {
            closeCursor(mValidCursor);
        }
		
    	mValidCursor = cursor;
	}
	
	void trackInvalidCursor(final Cursor cursor, final ContentObserver observer) {
		if (mValidCursor != null && mValidCursor != cursor && mRegistered.contains(mValidCursor)) {
			mValidCursor.unregisterContentObserver(observer);
			mRegistered.remove(mValidCursor);
		}
		
		if (mInvalidCursor != cursor) {
			closeCursor(mInvalidCursor);
		}
		
		mInvalidCursor = cursor;
	}
	
	void closeCursor(final Cursor cursor) {
    	if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            mRegistered.remove(cursor);
        }
    }

	void reset() {
		closeCursor(mValidCursor);
        closeCursor(mInvalidCursor);
        
        mValidCursor = null;
        mInvalidCursor = null;
	}
}