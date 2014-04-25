package com.xtreme.rest.providers;

import android.database.MatrixCursor;
import android.os.Bundle;

public class RestMatrixCursor extends MatrixCursor {

	public RestMatrixCursor(final String[] columnNames) {
		super(columnNames);
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
