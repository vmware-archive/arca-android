package com.appnet.app.accounts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AppNetAuthenticatorService extends Service {

	private AppNetAuthenticator mAccountAuthenticator;

	@Override
	public void onCreate() {
		super.onCreate();
		
		if (mAccountAuthenticator == null) { 
			mAccountAuthenticator = new AppNetAuthenticator(this);
		}
	}
	
	@Override
	public IBinder onBind(final Intent intent) {
		return mAccountAuthenticator.getIBinder();
	}
}
