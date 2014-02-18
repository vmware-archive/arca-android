package com.xtreme.rest;

import android.content.ContentResolver;
import android.content.Context;

import com.xtreme.rest.RestQueryValidator.ContentState;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.dispatcher.RequestExecutor;

public interface RestExecutor extends RequestExecutor {
	public void setValidator(final RestQueryValidator validator);
	
	public static class DefaultRestExecutor extends DefaultRequestExecutor implements RestExecutor {

		private RestQueryValidator mValidator;
		private final Context mContext;

		public DefaultRestExecutor(final ContentResolver resolver, final Context context) {
			super(resolver);
			mContext = context;
		}
		
		@Override
		public void setValidator(final RestQueryValidator validator) {
			mValidator = validator;
		}
		
		@Override
		public QueryResult execute(final Query request) {
			final QueryResult result = super.execute(request);
			
			if (mValidator != null) {
				validate(mContext, mValidator, request, result);
			}
			return result;
		}

		private static void validate(final Context context, final RestQueryValidator validator, final Query request, final QueryResult result) {
			final ContentState state = validator.validate(request, result);
			result.setIsValid(state == ContentState.VALID || state == ContentState.EXPIRED);
			
			final boolean refresh = state != ContentState.VALID;
			final boolean refreshing = refresh && validator.refresh(context, request, result);
			result.setIsRefreshing(refreshing);
		}
	}
}
