package com.xtreme.rest.service;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class OperationHandler extends Handler implements OperationObserver {

	private static final int MSG_NOTIFY_COMPLETE = 100;

	public static enum HandlerState {
		IDLE, RUNNING
	}
	
	public static interface OnStateChangeListener {
		public void onStateChanged(HandlerState state);
	}
	
	private final Set<Operation> mOperations = new HashSet<Operation>();
	private final RequestExecutor mExecutor;
	private final Context mContext;
	
	private OnStateChangeListener mListener;
	
	public OperationHandler(final Context context, final RequestExecutor executor) {
		mContext = context;
		mExecutor = executor;
	}
	
	/**
	 * Classes can listen for {@link HandlerState} change events by registering
	 * an {@link OnStateChangeListener}. They will be notified when the handler
	 * starts running and when it returns to an idle state.
	 * 
	 * @param listener
	 */
	public void setOnStateChangeListener(final OnStateChangeListener listener) {
		mListener = listener;
	}
	
	/**
	 * Execute a given {@link Operation}. The operation will be given 
	 * a {@link RequestExecutor}, {@link Context} and {@link OperationObserver}
	 * before it is executed.    
	 * 
	 * @param operation
	 * @return A boolean indicating whether the operation was executed.
	 */
	public boolean start(final Operation operation) {
		if (mOperations.isEmpty()) {
			notifyRunning();
		}
		
		if (mOperations.add(operation)) {
			operation.setContext(mContext);
			operation.setRequestExecutor(mExecutor);
			operation.setOperationObserver(this);
			operation.execute();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * A callback for when an {@link Operation} finishes.</br>
	 * </br>
	 * Note: This method gets executed on the main thread.
	 * 
	 * @param operation
	 * @return A boolean indicating whether the handler is empty.
	 */
	public boolean finish(final Operation operation) {
		mOperations.remove(operation);

		if (mOperations.isEmpty()) {
			notifyEmpty();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method returns the list of currently executing operations.
	 * 
	 * @return A set of {@link Operation}s
	 */
	public Set<Operation> getOperations() {
		return mOperations;
	}

	/**
	 * A callback for when an operation completes. This method is called from the 
	 * thread in which the operation was executed. At this point the handler will 
	 * send itself a message so that it can perform {@link #finish(Operation)} 
	 * on the main thread.
	 * 
	 * @param operation
	 */
	@Override
	public void onOperationComplete(final Operation operation) {
		final Message message = obtainMessage(MSG_NOTIFY_COMPLETE, operation);
		sendMessage(message);
	}
	
	@Override
	public void handleMessage(final Message msg) {
		if (msg.what == MSG_NOTIFY_COMPLETE) {
			final Operation operation = (Operation) msg.obj;
			finish(operation);
		}
	}
	
	private void notifyRunning() {
		if (mListener != null) {
			mListener.onStateChanged(HandlerState.RUNNING);
		}
	}
	
	private void notifyEmpty() {
		if (mListener != null) {
			mListener.onStateChanged(HandlerState.IDLE);
		}
	}
}