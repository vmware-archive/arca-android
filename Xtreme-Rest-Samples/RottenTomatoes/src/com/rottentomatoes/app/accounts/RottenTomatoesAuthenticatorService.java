package com.rottentomatoes.app.accounts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RottenTomatoesAuthenticatorService extends Service {

	private RottenTomatoesAuthenticator mAccountAuthenticator;

	@Override
	public void onCreate() {
		super.onCreate();
		
		if (mAccountAuthenticator == null) { 
			mAccountAuthenticator = new RottenTomatoesAuthenticator(this);
		}
	}
	
	@Override
	public IBinder onBind(final Intent intent) {
		return mAccountAuthenticator.getIBinder();
	}
}
