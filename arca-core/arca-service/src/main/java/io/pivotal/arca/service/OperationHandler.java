package io.pivotal.arca.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import io.pivotal.arca.threading.Identifier;
import io.pivotal.arca.utils.Logger;

public class OperationHandler extends Handler implements OperationObserver {

    private static final int MSG_NOTIFY_COMPLETE = 100;

    public enum HandlerState {
        IDLE, RUNNING
    }

    public interface OnStateChangeListener {
        void onStateChanged(HandlerState state);
    }

    private final Map<Identifier<?>, Operation> mOperations = new HashMap<Identifier<?>, Operation>();
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

    public Map<Identifier<?>, Operation> getOperations() {
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

        final Identifier<?> identifier = operation.getIdentifier();

        if (!mOperations.containsKey(identifier)) {
            mOperations.put(identifier, operation);
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
        Logger.v("OperationHandler[%s] Operation[%s] finish", this, operation);

        final Identifier<?> identifier = operation.getIdentifier();

        mOperations.remove(identifier);

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

    public boolean cancel(final Operation operation) {
        Logger.v("OperationHandler[%s] Operation[%s] cancel", this, operation);

        final Identifier<?> identifier = operation.getIdentifier();

        if (mOperations.containsKey(identifier)) {
            mOperations.get(identifier).cancel();
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
