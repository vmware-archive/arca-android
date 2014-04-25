package com.xtreme.rest.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

public class ContentPrecacher {

	public static void execute(final Context context, final ContentRequest request) {
		new PrecacheTask(request, context).execute();
	}
	
	private static final class PrecacheTask extends AsyncTask<Void, Void, Void> {
		
		private final ContentRequest mContentRequest;
		private final Context mContext;

		private PrecacheTask(final ContentRequest contentRequest, final Context context) {
			mContentRequest = contentRequest;
			mContext = context;
		}

		@Override
		protected Void doInBackground(final Void... params) {
			
			final Uri contentUri = mContentRequest.getForceUpdateContentUri();
			final String[] projection = mContentRequest.getProjection();
			final String whereClause = mContentRequest.getWhereClause();
			final String[] whereArgs = mContentRequest.getWhereArgs();
			final String sortOrder = mContentRequest.getSortOrder();
			
			final ContentResolver resolver = mContext.getContentResolver();
			resolver.query(contentUri, projection, whereClause, whereArgs, sortOrder);
			
			return null;
		}
	}
}
