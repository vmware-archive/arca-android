package com.xtreme.rest.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.xtreme.rest.service.OperationHandler.HandlerState;
import com.xtreme.rest.service.OperationHandler.OnStateChangeListener;
import com.xtreme.rest.service.RequestExecutor.ThreadedRequestExecutor;
import com.xtreme.rest.utils.Logger;

/**
 * This class serves as the main entry point to the system, where {@link Operation}s are started, 
 * and their {@link Task}s execute. This {@link Service} runs as long as there are {@link Operation}s 
 * running and stops itself once the last one is done.</br>
 * </br>
 * Note: This class must be declared in the AndroidManifest.xml file as follows:</br>
 * {@code <service android:name="com.xtreme.rest.service.OperationService" android:exported="false" />}
 */
public class OperationService extends Service implements OnStateChangeListener {

	public static enum Action {
		START, CANCEL;
	}
	
	protected static interface Extras {
		public static final String ACTION = "action";
		public static final String OPERATION = "operation";
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
			final Intent intent = new Intent(context, OperationService.class);
			intent.putExtra(Extras.OPERATION, operation);
			intent.putExtra(Extras.ACTION, action);
			context.startService(intent);
			return true;
		} else {
			return false;
		}
	}

	private OperationHandler mHandler;
	private int mLatestStartId;

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.v("Created service %s", this.getClass());
		
		mHandler = onCreateOperationHandler();
	}

	protected OperationHandler onCreateOperationHandler() {
		final Context context = getApplicationContext();
		final RequestExecutor executor = new ThreadedRequestExecutor();
		final OperationHandler handler = new OperationHandler(context, executor);
		handler.setOnStateChangeListener(this);
		return handler;
	}
	
	@Override
	public void onDestroy() {
		Logger.v("Destroyed service %s", this.getClass());
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		Logger.v("Started service %s", this.getClass());
		final int start = super.onStartCommand(intent, flags, startId);
		mLatestStartId = startId;
		handleIntent(intent);
		return start;
	}

	private void handleIntent(final Intent intent) {
		if (intent != null && intent.hasExtra(Extras.OPERATION)) {
			final Action action = (Action) intent.getSerializableExtra(Extras.ACTION);
			final Operation operation = intent.getParcelableExtra(Extras.OPERATION);
			handleAction(action, operation);
		}
	}
	
	private void handleAction(final Action action, final Operation operation) {
		if (action == Action.START) {
			handleStart(operation);
		} else {
			throw new UnsupportedOperationException("This action has not yet been implemented.");
		}
	}

	private void handleStart(final Operation operation) {
		mHandler.start(operation);
	}
	
	@Override
	public void onStateChanged(final HandlerState state) {
		if (state == HandlerState.IDLE) {
			stopSelf(mLatestStartId);
		}
	}

}
