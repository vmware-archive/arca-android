package com.rottentomatoes.app.sync;

import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.rottentomatoes.app.operations.SyncMoviesOperation;
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;
import com.xtreme.rest.RestService;
import com.xtreme.rest.sync.RestSyncAdapter;

public class RottenTomatoesSyncAdapter extends RestSyncAdapter {

	private static final long MANUAL_SYNC_INTERVAL = 5 * 1000;

	public RottenTomatoesSyncAdapter(final Context context, final boolean autoInitialize) {
		super(context, autoInitialize);
	}

	@Override
	protected void onSetupSync(final Account account, final Bundle extras, final String authority, final ContentProviderClient provider) {

		final long lastSyncTime = getLastSyncTime(account, authority);

		if (System.currentTimeMillis() > (lastSyncTime + MANUAL_SYNC_INTERVAL)) { 
			ContentResolver.requestSync(account, authority, createMoviesBundle("box_office"));
			ContentResolver.requestSync(account, authority, createMoviesBundle("in_theaters"));
			ContentResolver.requestSync(account, authority, createMoviesBundle("opening"));
			ContentResolver.requestSync(account, authority, createMoviesBundle("upcoming"));
		}
	}
	
	@Override
	protected boolean onPerformSync(final Uri uri, final Account account, final Bundle extras, final String authority, final ContentProviderClient provider) {
		return RestService.start(getContext(), new SyncMoviesOperation(uri));
	}
	
	
	// =============================================
	

	private static Bundle createMoviesBundle(final String type) {
		final Bundle bundle = new Bundle();
		final Uri uri = Uri.withAppendedPath(RottenTomatoesContentProvider.Uris.MOVIES_URI, type);
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		bundle.putString(RestSyncAdapter.Extras.URI, uri.toString());
		return bundle;
	}
}
