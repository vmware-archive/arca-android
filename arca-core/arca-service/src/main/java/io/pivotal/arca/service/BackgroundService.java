package io.pivotal.arca.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import io.pivotal.arca.threading.Identifier;
import io.pivotal.arca.utils.Logger;

public class BackgroundService extends JobIntentService implements OperationHandler.OnStateChangeListener, OperationHandlerObserver {

    private static final int SEED = 1000;
    private static final int JOB_ID = new Random(SEED).nextInt();

    private static final Object LOCK = new Object();
    private static final HashMap<Identifier<?>, Operation> OPERATIONS = new HashMap<>();

    protected enum Action {
        START, CANCEL
    }

    protected interface Extras {
        String ACTION = "action";
        String OPERATION = "operation";
    }

    public static boolean contains(final Operation operation) {
        synchronized (LOCK) {
            return OPERATIONS.containsKey(operation.getIdentifier());
        }
    }

    public static void start(final Context context, final Operation operation) {
        final Intent intent = new Intent(context, OperationService.class);
        intent.putExtra(Extras.OPERATION, operation);
        intent.putExtra(Extras.ACTION, Action.START);
        enqueueWork(context, BackgroundService.class, JOB_ID, intent);
    }

    private OperationHandler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.v("Created service %s", this.getClass());

        mHandler = onCreateOperationHandler();

        clearOperations();
    }

    @Override
    public void onDestroy() {
        Logger.v("Destroyed service %s", this.getClass());
        super.onDestroy();
    }

    protected OperationHandler onCreateOperationHandler() {
        final Context context = getApplicationContext();
        final RequestExecutor executor = new RequestExecutor.ThreadedRequestExecutor();
        final OperationHandler handler = new OperationHandler(context, executor);
        handler.setOperationHandlerObserver(this);
        handler.setOnStateChangeListener(this);
        return handler;
    }

    @Override
    protected void onHandleWork(@NonNull final Intent intent) {
        Logger.v("Handle work service %s", this.getClass());
        if (intent.hasExtra(Extras.OPERATION)) {
            final Action action = (Action) intent.getSerializableExtra(Extras.ACTION);
            final Operation operation = intent.getParcelableExtra(Extras.OPERATION);
            handleAction(action, operation);
        }
    }

    protected void handleAction(final Action action, final Operation operation) {
        switch (action) {
            case START:
                mHandler.start(operation);
                break;

            case CANCEL:
                mHandler.cancel(operation);
                break;
        }
    }

    protected Collection<Operation> getOperations() {
        synchronized (LOCK) {
            return OPERATIONS.values();
        }
    }

    protected void clearOperations() {
        synchronized (LOCK) {
            OPERATIONS.clear();
        }
    }

    @Override
    public void onOperationStarted(final Operation operation) {
        synchronized (LOCK) {
            OPERATIONS.put(operation.getIdentifier(), operation);
        }
    }

    @Override
    public void onOperationFinished(final Operation operation) {
        synchronized (LOCK) {
            OPERATIONS.remove(operation.getIdentifier());
        }
    }

    @Override
    public void onStateChanged(final OperationHandler.HandlerState state) {
        if (state == OperationHandler.HandlerState.IDLE) {
            Logger.v("Idle service %s", this.getClass());
        }
    }
}
