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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import java.util.HashMap;

import io.pivotal.arca.utils.Logger;

public class OperationService extends Service implements OperationHandler.OnStateChangeListener, OperationHandlerObserver {

    private static final Object LOCK = new Object();
    private static final HashMap<Uri, Operation> OPERATIONS = new HashMap<Uri, Operation>();

    public static enum Action {
        START, CANCEL
    }

    protected static interface Extras {
        public static final String ACTION = "action";
        public static final String OPERATION = "operation";
    }

    public static boolean contains(final Operation operation) {
        synchronized (LOCK) {
            return OPERATIONS.containsKey(operation.getUri());
        }
    }

    public static boolean start(final Context context, final Operation operation) {
        return start(context, operation, Action.START);
    }

    public static boolean cancel(final Context context, final Operation operation) {
        return start(context, operation, Action.CANCEL);
    }

    private static boolean start(final Context context, final Operation operation, final Action action) {
        return context != null && operation != null && startService(context, operation, action);
    }

    private static boolean startService(final Context context, final Operation operation, final Action action) {
        final Intent intent = new Intent(context, OperationService.class);
        intent.putExtra(Extras.OPERATION, operation);
        intent.putExtra(Extras.ACTION, action);
        context.startService(intent);
        return true;
    }

    private OperationHandler mHandler;
    private int mLatestStartId;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.v("Created service %s", this.getClass());

        mHandler = onCreateOperationHandler();

        clearOperations();
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

    protected void handleIntent(final Intent intent) {
        if (intent != null && intent.hasExtra(Extras.OPERATION)) {
            final Action action = (Action) intent.getSerializableExtra(Extras.ACTION);
            final Operation operation = intent.getParcelableExtra(Extras.OPERATION);
            handleAction(action, operation);
        }
    }

    protected void handleAction(final Action action, final Operation operation) {
        if (action == Action.START) {
            handleStart(operation);

        } else if (action == Action.CANCEL) {
            handleCancel(operation);

        } else {
            throw new UnsupportedOperationException("This action has not yet been implemented.");
        }
    }

    protected void handleStart(final Operation operation) {
        if (mHandler != null) {
            mHandler.start(operation);
        }
    }

    protected void handleCancel(final Operation operation) {
        if (mHandler != null) {
            mHandler.cancel(operation);
        }
    }

    private void clearOperations() {
        synchronized (LOCK) {
            OPERATIONS.clear();
        }
    }

    @Override
    public void onOperationStarted(final Operation operation) {
        synchronized (LOCK) {
            final Uri uri = operation.getUri();
            OPERATIONS.put(uri, operation);
        }
    }

    @Override
    public void onOperationFinished(final Operation operation) {
        synchronized (LOCK) {
            final Uri uri = operation.getUri();
            OPERATIONS.remove(uri);
        }
    }

    @Override
    public void onStateChanged(final OperationHandler.HandlerState state) {
        if (state == OperationHandler.HandlerState.IDLE) {
            stopSelf(mLatestStartId);
        }
    }
}
