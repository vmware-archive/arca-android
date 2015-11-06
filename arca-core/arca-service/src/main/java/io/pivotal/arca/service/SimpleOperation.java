/*
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.pivotal.arca.threading.Identifier;

public abstract class SimpleOperation extends Operation {

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
        return new HashSet<Task<?>>(Arrays.asList(new InnerTask()));
    }

    public abstract ContentValues[] onExecute(final Context context) throws Exception;

    public void onPostExecute(final Context context, final ContentValues[] values) throws Exception {
        if (values != null) {
            context.getContentResolver().bulkInsert(getUri(), values);
        }
    }

    @Override
    public void onSuccess(final Context context, final List<Task<?>> completed) {
        context.getContentResolver().notifyChange(getUri(), null);
    }

    @Override
    public void onComplete(final Context context, final Results results) {

    }

    private final class InnerTask extends Task<ContentValues[]> {

        @Override
        public Identifier<?> onCreateIdentifier() {
            return SimpleOperation.this.onCreateIdentifier();
        }

        @Override
        public ContentValues[] onExecuteNetworking(final Context context) throws Exception {
            return SimpleOperation.this.onExecute(context);
        }

        @Override
        public void onExecuteProcessing(final Context context, final ContentValues[] data) throws Exception {
            SimpleOperation.this.onPostExecute(context, data);
        }
    }
}
