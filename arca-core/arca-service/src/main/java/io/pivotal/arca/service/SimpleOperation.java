package io.pivotal.arca.service;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import java.util.HashSet;
import java.util.Set;

import io.pivotal.arca.threading.Identifier;

public abstract class SimpleOperation<T> extends Operation {

    public SimpleOperation(final Uri uri, final Priority priority) {
        super(uri, priority);
    }

    public SimpleOperation(final Uri uri) {
        super(uri);
    }

    public SimpleOperation(final Parcel in) {
        super(in);
    }

    @Override
    public final Set<Task<?>> onCreateTasks() {
        final Set<Task<?>> set = new HashSet<Task<?>>();
        set.add(new InnerTask());
        return set;
    }

    public abstract Identifier<?> onCreateIdentifier();

    public abstract T onExecuteNetworking(final Context context) throws Exception;

    public abstract void onExecuteProcessing(Context context, T data) throws Exception;


    private final class InnerTask extends Task<T> {

        private InnerTask() {}

        @Override
        public Identifier<?> onCreateIdentifier() {
            return SimpleOperation.this.onCreateIdentifier();
        }

        @Override
        public T onExecuteNetworking(final Context context) throws Exception {
            return SimpleOperation.this.onExecuteNetworking(context);
        }

        @Override
        public void onExecuteProcessing(Context context, T data) throws Exception {
            SimpleOperation.this.onExecuteProcessing(context, data);
        }
    }
}
