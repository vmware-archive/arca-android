package com.xtreme.rest;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.OperationObserver;
import com.xtreme.rest.service.OperationService;
import com.xtreme.rest.service.RequestExecutor;

public class RestService extends OperationService {

	/**
	 * Binds to the {@link RestService}. This method uses {@link Context#bindService(Intent, ServiceConnection, int)}
	 * to bind to the service, passing {@link Context.BIND_AUTO_CREATE} as a flag.
	 * 
	 * @param context The {@link Context} in which this service should be bound
	 * @param connection The {@link ServiceConnection} that receives life cycle events
	 * @return <code>true</code> if the {@link RestService} binding was successful, <code>false</code> otherwise
	 */
	public static boolean bind(final Context context, final ServiceConnection connection) {
		final Intent intent = new Intent(context, RestService.class);
		return context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * Starts an {@link Operation}. The {@link Operation} is given a {@link Context}, 
	 * {@link OperationObserver} and {@link RequestExecutor} before being executed.</br>
	 * </br>
	 * Note: This method uses {@link Context#startService(Intent)} method to deliver the message.
	 * 
	 * @param context The {@link Context} in which this service should be started
	 * @param operation The {@link Operation} to be started
	 * @return <code>true</code> if the {@link Operation} was started, <code>false</code> otherwise
	 */
	public static boolean start(final Context context, final Operation operation) {
		return startService(context, operation, Action.START);
	}

	/**
	 * Attempts to cancel a currently running {@link Operation}.</br>
	 * </br>
	 * Note: This method uses {@link Context#startService(Intent)} method to deliver the message.
	 * 
	 * @param context The {@link Context} in which this service should be started
	 * @param operation The {@link Operation} to be canceled
	 * @return <code>false<code> if the cancel cannot be attempted, <code>true</code> otherwise
	 */
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