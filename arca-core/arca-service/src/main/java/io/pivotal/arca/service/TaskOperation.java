package io.pivotal.arca.service;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.pivotal.arca.threading.Identifier;

public abstract class TaskOperation<T> extends Operation {

    public TaskOperation(final Uri uri, final Priority priority) {
        super(uri, priority);
    }

    public TaskOperation(final Uri uri) {
        super(uri);
    }

    public TaskOperation(final Parcel in) {
        super(in);
    }

    @Override
    public final Set<Task<?>> onCreateTasks() {
        return new HashSet<Task<?>>(Collections.singletonList(new InnerTask()));
    }

    public abstract T onExecute(final Context context) throws Exception;

    public void onPostExecute(final Context context, final T data) throws Exception {}


    private final class InnerTask extends Task<T> {

        @Override
        public Identifier<?> onCreateIdentifier() {
            return TaskOperation.this.onCreateIdentifier();
        }

        @Override
        public T onExecuteNetworking(final Context context) throws Exception {
            return TaskOperation.this.onExecute(context);
        }

        @Override
        public void onExecuteProcessing(final Context context, final T data) throws Exception {
            TaskOperation.this.onPostExecute(context, data);
        }
    }
}
