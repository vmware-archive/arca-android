package com.xtreme.rest.provider;

import android.database.Cursor;
import android.database.MergeCursor;
import android.os.Bundle;

public class RestMergeCursor extends MergeCursor {

	public RestMergeCursor(final Cursor[] cursors) {
		super(cursors);
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
