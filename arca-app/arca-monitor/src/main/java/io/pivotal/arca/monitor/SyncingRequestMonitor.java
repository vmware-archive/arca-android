package io.pivotal.arca.monitor;

import android.content.Context;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import io.pivotal.arca.dispatcher.Batch;
import io.pivotal.arca.dispatcher.BatchResult;
import io.pivotal.arca.dispatcher.Delete;
import io.pivotal.arca.dispatcher.DeleteResult;
import io.pivotal.arca.dispatcher.Insert;
import io.pivotal.arca.dispatcher.InsertResult;
import io.pivotal.arca.dispatcher.Query;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.dispatcher.Update;
import io.pivotal.arca.dispatcher.UpdateResult;

public class SyncingRequestMonitor extends NotifyingRequestMonitor {

    public static final int DEFAULT_INTERVAL_IN_SECONDS = 30;

    private final Map<Uri, Long> mLastQuerySync = new HashMap<Uri, Long>();
    private final int mIntervalInMilliseconds;

    public SyncingRequestMonitor(final Uri uri) {
        this(uri, DEFAULT_INTERVAL_IN_SECONDS);
    }

    public SyncingRequestMonitor(final Uri uri, final int stalenessIntervalInSeconds) {
        super(uri);
        mIntervalInMilliseconds = stalenessIntervalInSeconds * 1000;
    }

    public SyncingRequestMonitor(final Uri[] uris) {
        this(uris, DEFAULT_INTERVAL_IN_SECONDS);
    }

    public SyncingRequestMonitor(final Uri[] uris, final int stalenessIntervalInSeconds) {
        super(uris);
        mIntervalInMilliseconds = stalenessIntervalInSeconds * 1000;
    }

    @Override
    public int onPostExecute(final Context context, final Query request, final QueryResult result) {
        if (shouldSync(context, request, result) && onSync(context, request, result)) {
            mLastQuerySync.put(request.getUri(), System.currentTimeMillis());
            return Flags.DATA_SYNCING;
        } else {
            return super.onPostExecute(context, request, result);
        }
    }

    public boolean shouldSync(final Context context, final Query request, final QueryResult result) {
        final Long lastSync = mLastQuerySync.get(request.getUri());
        return lastSync == null || System.currentTimeMillis() > (lastSync + mIntervalInMilliseconds);
    }

    public boolean onSync(final Context context, final Query request, final QueryResult result) {
        return false;
    }

    @Override
    public int onPostExecute(final Context context, final Update request, final UpdateResult result) {
        final boolean syncing = shouldSync(context, request, result) && onSync(context, request, result);
        return syncing ? Flags.DATA_SYNCING : super.onPostExecute(context, request, result);
    }

    public boolean shouldSync(final Context context, final Update request, final UpdateResult result) {
        return true;
    }

    public boolean onSync(final Context context, final Update request, final UpdateResult result) {
        return false;
    }

    @Override
    public int onPostExecute(final Context context, final Insert request, final InsertResult result) {
        final boolean syncing = shouldSync(context, request, result) && onSync(context, request, result);
        return syncing ? Flags.DATA_SYNCING : super.onPostExecute(context, request, result);
    }

    public boolean shouldSync(final Context context, final Insert request, final InsertResult result) {
        return true;
    }

    public boolean onSync(final Context context, final Insert request, final InsertResult result) {
        return false;
    }

    @Override
    public int onPostExecute(final Context context, final Delete request, final DeleteResult result) {
        final boolean syncing = shouldSync(context, request, result) && onSync(context, request, result);
        return syncing ? Flags.DATA_SYNCING : super.onPostExecute(context, request, result);
    }

    public boolean shouldSync(final Context context, final Delete request, final DeleteResult result) {
        return true;
    }

    public boolean onSync(final Context context, final Delete request, final DeleteResult result) {
        return false;
    }

    @Override
    public int onPostExecute(final Context context, final Batch request, final BatchResult result) {
        final boolean syncing = shouldSync(context, request, result) && onSync(context, request, result);
        return syncing ? Flags.DATA_SYNCING : super.onPostExecute(context, request, result);
    }

    public boolean shouldSync(final Context context, final Batch request, final BatchResult result) {
        return true;
    }

    public boolean onSync(final Context context, final Batch request, final BatchResult result) {
        return false;
    }
}
