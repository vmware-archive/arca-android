package com.xtreme.rest.loader;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.xtreme.rest.utils.CursorUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernCursorLoader extends AsyncTaskLoader<Cursor> {

	private final ForceLoadContentObserver mObserver;
	private final CursorTracker mTracker;
	
	private ContentRequest mRequest;
	
	public ModernCursorLoader(final Context context) {
		super(context);
		mObserver = new ForceLoadContentObserver();
		mTracker = new CursorTracker();
	}
	
	public synchronized void setRequest(final ContentRequest request) {
		mRequest = request;
	}
	
	@Override
    protected void onStartLoading() {
		final Cursor cursor = mTracker.getCursor();
        if (cursor != null) {
            deliverResult(cursor);
        }
        if (takeContentChanged() || cursor == null) {
            forceLoad();
        }
    }

    @Override
    public synchronized Cursor loadInBackground() {
    	final Uri uri = mRequest.getForceUpdateContentUri();
		final String []projection = mRequest.getProjection();
		final String whereClause = mRequest.getWhereClause();
		final String[] whereArgs = mRequest.getWhereArgs();
		final String sortOrder = mRequest.getSortOrder();
		
    	final ContentResolver resolver = getContext().getContentResolver(); 
        final Cursor cursor = resolver.query(uri, projection, whereClause, whereArgs, sortOrder);
        
        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
        }
        
        return cursor;
    }

    @Override
    public void onCanceled(final Cursor cursor) {
        closeCursor(cursor);
    }
    
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
    
    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        mTracker.reset();
    }
    
    @Override
    public void deliverResult(final Cursor cursor) {
    	mTracker.registerObserver(cursor, mObserver);
    	
    	if (isReset()) {
            closeCursor(cursor);
        } else {
        	checkDelivery(cursor);
        }
    }

	private void checkDelivery(final Cursor cursor) {
		final boolean isInvalid = CursorUtils.isInvalid(cursor);
        
    	if (isInvalid) {
    		trackInvalidCursor(cursor);
    		
    	} else {
    		deliverCursor(cursor);
    		trackValidCursor(cursor);
    	}
	}

	private void deliverCursor(final Cursor cursor) {
    	if (isStarted()) {
            super.deliverResult(cursor);
        }
	}
	
	private void trackValidCursor(final Cursor cursor) {
		mTracker.trackValidCursor(cursor);
	}
	
	private void trackInvalidCursor(final Cursor cursor) {
		mTracker.trackInvalidCursor(cursor, mObserver);
	}
	
	private void closeCursor(final Cursor cursor) {
    	mTracker.closeCursor(cursor);
    }
}