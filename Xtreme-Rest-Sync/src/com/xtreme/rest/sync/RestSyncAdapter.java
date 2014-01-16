package com.xtreme.rest.sync;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.content.SyncStats;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.xtreme.rest.utils.Logger;

public abstract class RestSyncAdapter extends AbstractThreadedSyncAdapter {
	
	protected abstract void onSetupSync(final Account account, final Bundle extras, final String authority, final ContentProviderClient provider);
	protected abstract boolean onPerformSync(final Uri uri, final Account account, final Bundle extras, final String authority, final ContentProviderClient provider);
	
	public static final class Extras {
		public static final String URI = "uri";
	}
	
	public RestSyncAdapter(final Context context, final boolean autoInitialize) {
		super(context, autoInitialize);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public RestSyncAdapter(final Context context, final boolean autoInitialize, final boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
	}
	
	@Override
	public final void onPerformSync(final Account account, final Bundle extras, final String authority, final ContentProviderClient provider, final SyncResult syncResult) {
		final String uriString = extras.getString(Extras.URI);

		Logger.v(":::::::::::: [sync uri : %s] ::::::::::::", uriString);
		
		for (final String key : extras.keySet()) {
			Logger.v("[sync extra] %s : %s", key, extras.get(key));
		}
		
		if (uriString == null) {
			onSetupSync(account, extras, authority, provider);
			
		} else {
			performSync(Uri.parse(uriString), account, extras, authority, provider, syncResult);
		}

		Logger.v(":::::::::::: [sync complete : %s] ::::::::::::", syncResult);
	}

	private void performSync(final Uri uri, final Account account, final Bundle extras, final String authority, final ContentProviderClient provider, final SyncResult syncResult) {
		final boolean syncing = onPerformSync(uri, account, extras, authority, provider);
		if (syncing) {
			awaitStatsBroadcast(uri, syncResult);
		}
	}
	
	private static void awaitStatsBroadcast(final Uri uri, final SyncResult syncResult) {
		try {
			final RestSynchronizer synchronizer = new RestSynchronizer();
			final SyncStats stats = synchronizer.awaitStatsBroadcast(uri);
			updateSyncResultStats(syncResult, stats);
		} catch (final InterruptedException e) {
			Logger.ex(e);
		}
	}
	
	private static void updateSyncResultStats(final SyncResult syncResult, final SyncStats stats) {
		if (syncResult != null && stats != null) {
			syncResult.stats.numAuthExceptions += stats.numAuthExceptions;
			syncResult.stats.numConflictDetectedExceptions += stats.numConflictDetectedExceptions;
			syncResult.stats.numIoExceptions += stats.numIoExceptions;
			syncResult.stats.numParseExceptions += stats.numParseExceptions;
			syncResult.stats.numSkippedEntries += stats.numSkippedEntries;
			syncResult.stats.numUpdates += stats.numUpdates;
			syncResult.stats.numDeletes += stats.numDeletes;
			syncResult.stats.numEntries += stats.numEntries;
			syncResult.stats.numInserts += stats.numInserts;
		}	
	}
	
	protected long getLastSyncTime(final Account account, final String authority) {
	    try {
	        final Method getSyncStatus = ContentResolver.class.getMethod("getSyncStatus", Account.class, String.class);
            final Object syncStatus = getSyncStatus.invoke(null, account, authority);
            final Field lastSuccessTime = syncStatus.getClass().getField("lastSuccessTime");
            return lastSuccessTime.getLong(syncStatus);
	    } catch (final Exception e) {
	    	Logger.ex(e);
	    	return 0;
	    }
	}
}
