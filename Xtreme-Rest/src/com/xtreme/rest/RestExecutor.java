package com.xtreme.rest;

import android.content.ContentResolver;
import android.content.Context;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.dispatcher.RequestExecutor;
import com.xtreme.rest.validator.ContentState;
import com.xtreme.rest.validator.QueryValidator;

public interface RestExecutor extends RequestExecutor {
	public void addValidator(final QueryValidator validator);
	
	public static class DefaultRestExecutor extends DefaultRequestExecutor implements RestExecutor {

		private QueryValidator mValidator;
		private final Context mContext;

		public DefaultRestExecutor(final ContentResolver resolver, final Context context) {
			super(resolver);
			mContext = context;
		}
		
		@Override
		public void addValidator(final QueryValidator validator) {
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

		private static void validate(final Context context, final QueryValidator validator, final Query request, final QueryResult result) {
			final ContentState state = validator.validate(request, result);
			result.setIsValid(state == ContentState.VALID || state == ContentState.EXPIRED);
			
			final boolean refresh = state != ContentState.VALID;
			final boolean refreshing = refresh && validator.refresh(context, request, result);
			result.setIsRefreshing(refreshing);
		}
	}
}
