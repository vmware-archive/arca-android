package com.xtreme.rest.fragments;

import android.content.ContentResolver;
import android.content.Context;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.dispatcher.RequestExecutor;
import com.xtreme.rest.fragments.RestQueryVerifier.ContentState;

public interface RestExecutor extends RequestExecutor {
	public void setQueryVerifier(final RestQueryVerifier verifier);
	public RestQueryVerifier getQueryVerifier();
	
	public static class DefaultRestExecutor extends DefaultRequestExecutor implements RestExecutor {

		private RestQueryVerifier mVerifier;
		private final Context mContext;

		public DefaultRestExecutor(final ContentResolver resolver, final Context context) {
			super(resolver);
			mContext = context;
		}
		
		@Override
		public void setQueryVerifier(final RestQueryVerifier verifier) {
			mVerifier = verifier;
		}
		
		@Override
		public RestQueryVerifier getQueryVerifier() {
			return mVerifier;
		}
		
		@Override
		public QueryResult execute(final Query request) {
			final QueryResult result = super.execute(request);
			
			if (mVerifier != null) {
				verify(mVerifier, mContext, request, result);
			}
			return result;
		}

		private static void verify(final RestQueryVerifier verifier, final Context context, final Query request, final QueryResult result) {
			final ContentState state = verifier.verify(context, request, result);
			result.setIsValid(state == ContentState.VALID || state == ContentState.EXPIRED);
			
			final boolean refresh = state != ContentState.VALID;
			final boolean refreshing = refresh && verifier.refresh(context, request, result);
			result.setIsRefreshing(refreshing);
		}
	}
}
