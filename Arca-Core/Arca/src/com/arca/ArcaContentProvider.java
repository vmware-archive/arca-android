package com.arca;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.arca.ArcaService.ArcaBinder;
import com.arca.provider.DatabaseProvider;
import com.arca.utils.Logger;

public class ArcaContentProvider extends DatabaseProvider implements ServiceConnection {

	private ArcaService mService;

	@Override
	public boolean onCreate() {
		return bindService();
	}

	private boolean bindService() {
		return ArcaService.bind(getContext(), this);
	}
	
	protected ArcaService getService() {
		return mService;
	}
	
	protected boolean isServiceConnected() {
		return mService != null;
	}
	
	// ==================================
	
	@Override
	public void onServiceConnected(final ComponentName name, final IBinder service) {
		final ArcaBinder binder = (ArcaBinder) service;
		Logger.v("onServiceConnected %s", this.getClass());
		mService = binder.getService();
	}
	
	@Override
	public void onServiceDisconnected(final ComponentName name) {
		Logger.v("onServiceDisconnected %s", this.getClass()); 
		mService = null;
	}
}
