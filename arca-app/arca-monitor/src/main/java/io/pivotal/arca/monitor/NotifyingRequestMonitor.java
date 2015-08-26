/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.monitor;

import android.content.Context;
import android.net.Uri;

import io.pivotal.arca.dispatcher.Delete;
import io.pivotal.arca.dispatcher.DeleteResult;
import io.pivotal.arca.dispatcher.Insert;
import io.pivotal.arca.dispatcher.InsertResult;
import io.pivotal.arca.dispatcher.Query;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.dispatcher.Update;
import io.pivotal.arca.dispatcher.UpdateResult;

public class NotifyingRequestMonitor extends RequestMonitor.AbstractRequestMonitor {

    private final Uri[] mUris;

    public NotifyingRequestMonitor(final Uri uri) {
        mUris = new Uri[] { uri };
    }

    public NotifyingRequestMonitor(final Uri[] uris) {
        mUris = uris;
    }

    @Override
    public int onPostExecute(final Context context, final Query request, final QueryResult result) {
        notifyChange(context);
        return super.onPostExecute(context, request, result);
    }

    @Override
    public int onPostExecute(final Context context, final Update request, final UpdateResult result) {
        notifyChange(context);
        return super.onPostExecute(context, request, result);
    }

    @Override
    public int onPostExecute(final Context context, final Insert request, final InsertResult result) {
        notifyChange(context);
        return super.onPostExecute(context, request, result);
    }

    @Override
    public int onPostExecute(final Context context, final Delete request, final DeleteResult result) {
        notifyChange(context);
        return super.onPostExecute(context, request, result);
    }

    protected void notifyChange(final Context context) {
        if (mUris != null) {
            for (final Uri uri : mUris) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }
    }

}
