package com.rottentomatoes.app.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class RottenTomatoesSyncService extends Service {

	private static RottenTomatoesSyncAdapter sSyncAdapter;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		synchronized (this) {
			if (sSyncAdapter == null) {
				final Context context = getApplicationContext();
				sSyncAdapter = new RottenTomatoesSyncAdapter(context, true);
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
