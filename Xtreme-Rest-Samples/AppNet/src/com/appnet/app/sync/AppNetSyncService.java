package com.appnet.app.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class AppNetSyncService extends Service {

	private static AppNetSyncAdapter sSyncAdapter;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		synchronized (this) {
			if (sSyncAdapter == null) {
				final Context context = getApplicationContext();
				sSyncAdapter = new AppNetSyncAdapter(context, true);
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
