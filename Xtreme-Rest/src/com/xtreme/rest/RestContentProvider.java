package com.xtreme.rest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;

import com.xtreme.rest.RestService.RestBinder;
import com.xtreme.rest.provider.DatabaseProvider;
import com.xtreme.rest.service.Logger;

public class RestContentProvider extends DatabaseProvider implements ServiceConnection {

	private RestService mService;

	@Override
	public boolean onCreate() {
		return bindService();
	}

	private boolean bindService() {
		final Intent intent = new Intent(getContext(), RestService.class);
		return getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
	}
	
	protected RestService getService() {
		return mService;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		final Cursor cursor = super.query(uri, projection, selection, selectionArgs, sortOrder);
//		final ValidatorState state = validateQuery(uri, cursor);
		return cursor;
	}

//	private ValidatorState validateQuery(final Uri uri, final Cursor cursor) {
//		final RestService service = getService();
//		if (service != null) {
//			return service.validateQuery(uri, cursor);
//		} else {
//			return ValidatorState.VALID;
//		}
//	}
	
	
	// ==================================
	
	
	@Override
	public void onServiceConnected(final ComponentName name, final IBinder service) {
		final RestBinder binder = (RestBinder) service;
		Logger.v("onServiceConnected %s", this.getClass());
		mService = binder.getService();
	}
	
	@Override
	public void onServiceDisconnected(final ComponentName name) {
		Logger.v("onServiceDisconnected %s", this.getClass());
		mService = null;
	}

}
