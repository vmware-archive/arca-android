package com.xtreme.rest;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.OperationService;

public class RestService extends OperationService {

	public static boolean bind(final Context context, final ServiceConnection connection) {
		final Intent intent = new Intent(context, RestService.class);
		return context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}
	
	public static boolean start(final Context context, final Operation operation) {
		return startService(context, operation, Action.START);
	}

	public static boolean cancel(final Context context, final Operation operation) {
		return startService(context, operation, Action.CANCEL);
	}

	private static boolean startService(final Context context, final Operation operation, final Action action) {
		if (context != null && operation != null) {
			final Intent intent = new Intent(context, RestService.class);
			intent.putExtra(Extras.OPERATION, operation);
			intent.putExtra(Extras.ACTION, action);
			context.startService(intent);
			return true;
		} else {
			return false;
		}
	}
	
	private final IBinder mBinder = new RestBinder();
	
	@Override
	public IBinder onBind(final Intent intent) {
		return mBinder;
	}
	
	public class RestBinder extends Binder {

		public RestService getService() {
            return RestService.this;
        }
    }
}