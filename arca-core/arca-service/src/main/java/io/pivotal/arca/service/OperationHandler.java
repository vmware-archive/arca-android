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
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import io.pivotal.arca.utils.Logger;

public class OperationHandler extends Handler implements OperationObserver {

    private static final int MSG_NOTIFY_COMPLETE = 100;

    public static enum HandlerState {
        IDLE, RUNNING
    }

    public static interface OnStateChangeListener {
        public void onStateChanged(HandlerState state);
    }

    private final Map<Uri, Operation> mOperations = new HashMap<Uri, Operation>();
    private final RequestExecutor mExecutor;
    private final Context mContext;

    private OperationHandlerObserver mObserver;
    private OnStateChangeListener mListener;

    public OperationHandler(final Context context, final RequestExecutor executor) {
        mContext = context;
        mExecutor = executor;
    }

    public void setOperationHandlerObserver(final OperationHandlerObserver observer) {
        mObserver = observer;
    }

    public void setOnStateChangeListener(final OnStateChangeListener listener) {
        mListener = listener;
    }

    public Map<Uri, Operation> getOperations() {
        return mOperations;
    }

    public boolean start(final Operation operation) {
        Logger.v("OperationHandler[%s] Operation[%s] start", this, operation);

        if (mOperations.isEmpty()) {
            notifyRunning();
        }

        if (mObserver != null) {
            mObserver.onOperationStarted(operation);
        }

        final Uri uri = operation.getUri();

        if (!mOperations.containsKey(uri)) {
            mOperations.put(uri, operation);
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
        Logger.v("OperationHandler[%s] Operation[%s] cancel", this, operation);

        final Uri uri = operation.getUri();

        if (mOperations.containsKey(uri)) {
            mOperations.get(uri).cancel();
            return true;
        } else {
            return false;
        }
    }

    public boolean finish(final Operation operation) {
        Logger.v("OperationHandler[%s] Operation[%s] finish", this, operation);

        final Uri uri = operation.getUri();

        mOperations.remove(uri);

        if (mObserver != null) {
            mObserver.onOperationFinished(operation);
        }

        if (mOperations.isEmpty()) {
            notifyEmpty();
            return true;
        } else {
            return false;
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

    @Override
    public void handleMessage(final Message msg) {
        if (msg.what == MSG_NOTIFY_COMPLETE) {
            final Operation operation = (Operation) msg.obj;
            finish(operation);
        }
    }

    @Override
    public void onOperationComplete(final Operation operation) {
        Logger.v("OperationHandler[%s] Operation[%s] complete", this, operation);
        final Message message = obtainMessage(MSG_NOTIFY_COMPLETE, operation);
        sendMessage(message);
    }
}