/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.HashSet;
import java.util.Set;

public class OperationHandler extends Handler implements OperationObserver {

	private static final int MSG_NOTIFY_COMPLETE = 100;
    protected static final int MSG_NOTIFY_CANCELLED = 101;

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

    public boolean cancel(final Operation operation) {
        if (!mOperations.isEmpty() && mOperations.contains(operation)) {
            operation.cancel();
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
    public void onOperationCancelled(Operation operation) {
        final Message message = obtainMessage(MSG_NOTIFY_CANCELLED, operation);
        sendMessage(message);
    }

    @Override
	public void handleMessage(final Message msg) {
		if (msg.what == MSG_NOTIFY_COMPLETE || msg.what == MSG_NOTIFY_CANCELLED) {
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