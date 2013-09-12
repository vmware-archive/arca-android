package com.xtreme.rest.service;

import java.util.HashSet;
import java.util.Set;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * This class serves as the main entry point to the system, where {@link Operation}s are started, and their {@link Task}s execute.
 * This {@link Service} runs as long as there are {@link Operation}s running and stops itself once the last one is done.</br>
 * </br>
 * Note: This class must be declared in the AndroidManifest.xml file as follows:</br>
 * {@code <service android:name="com.xtreme.rest.service.RestService" android:exported="false" />}
 */
public class RestService extends Service {

	private static enum Action {
		START, CANCEL;
	}
	
	private static final class Extras {
		private static final String ACTION = "action";
		private static final String OPERATION = "operation";
	}

	/**
	 * Starts an {@link Operation}. The {@link Operation} is given a {@link Context}, 
	 * {@link OperationObserver} and {@link RequestHandler} before being executed.</br>
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
		if (operation != null && context != null) {
			final Intent intent = new Intent(context, RestService.class);
			intent.putExtra(Extras.OPERATION, operation);
			intent.putExtra(Extras.ACTION, action);
			context.startService(intent);
			return true;
		} else {
			return false;
		}
	}

	private final Set<Operation> mOperations = new HashSet<Operation>();
	
	private OperationObserver mObserver;
	private RequestHandler mHandler;
	private int mLatestStartId;

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.v("Creating service %s", this.getClass());
		mObserver = new OperationHandler(this);
		mHandler = new RequestExecutor();
	}
	
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
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

	public void handleStart(final Operation operation) {
		mOperations.add(operation);
		operation.setContext(getApplicationContext());
		operation.setRequestHandler(mHandler);
		operation.setOperationObserver(mObserver);
		operation.execute();
	}

	public void handleFinish(final Operation operation) {
		mOperations.remove(operation);

		if (mOperations.isEmpty()) {
			stopSelf(mLatestStartId);
		}
	}
	
	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Logger.v("Destroying service %s", this.getClass());
		super.onDestroy();
	}
}
