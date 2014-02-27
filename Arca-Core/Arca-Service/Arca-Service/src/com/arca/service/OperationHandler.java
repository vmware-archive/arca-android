package com.arca.service;

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
	
	public void setOnStateChangeListener(final OnStateChangeListener listener) {
		mListener = listener;
	}
	
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

	public boolean finish(final Operation operation) {
		mOperations.remove(operation);

		if (mOperations.isEmpty()) {
			notifyEmpty();
			return true;
		} else {
			return false;
		}
	}
	
	public Set<Operation> getOperations() {
		return mOperations;
	}

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