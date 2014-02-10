package com.xtreme.rest;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.xtreme.rest.RestService.RestBinder;
import com.xtreme.rest.provider.DatabaseProvider;
import com.xtreme.rest.utils.Logger;

public class RestContentProvider extends DatabaseProvider implements ServiceConnection {

	private RestService mService;

	@Override
	public boolean onCreate() {
		return bindService();
	}

	private boolean bindService() {
		return RestService.bind(getContext(), this);
	}
	
	protected RestService getService() {
		return mService;
	}
	
	protected boolean isServiceConnected() {
		return mService != null;
	}
	
	
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
