package com.xtreme.rest.monitor;

import android.content.ContentResolver;
import android.content.Context;

import com.xtreme.rest.dispatcher.Delete;
import com.xtreme.rest.dispatcher.DeleteResult;
import com.xtreme.rest.dispatcher.Insert;
import com.xtreme.rest.dispatcher.InsertResult;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.dispatcher.RequestExecutor;
import com.xtreme.rest.dispatcher.Result;
import com.xtreme.rest.dispatcher.Update;
import com.xtreme.rest.dispatcher.UpdateResult;
import com.xtreme.rest.monitor.RequestMonitor.Flags;

public interface RestExecutor extends RequestExecutor {
	
	public void setRequestMonitor(final RequestMonitor monitor);
	
	public RequestMonitor getRequestMonitor();
	
	public static class DefaultRestExecutor extends DefaultRequestExecutor implements RestExecutor {

		private RequestMonitor mMonitor;
		private final Context mContext;

		public DefaultRestExecutor(final ContentResolver resolver, final Context context) {
			super(resolver);
			mContext = context;
		}
		
		@Override
		public void setRequestMonitor(final RequestMonitor verifier) {
			mMonitor = verifier;
		}
		
		@Override
		public RequestMonitor getRequestMonitor() {
			return mMonitor;
		}
		
		@Override
		public QueryResult execute(final Query request) {
			final RequestMonitor monitor = getRequestMonitor();
			
			int flags = 0;
			
			if (monitor != null) {
				flags = flags | monitor.onPreExecute(mContext, request);
			}
			
			final QueryResult result = super.execute(request);
			
			if (monitor != null) {
				flags = flags | monitor.onPostExecute(mContext, request, result);
			}
			
			return (QueryResult) appendFlags(result, flags);
		}
		
		@Override
		public UpdateResult execute(final Update request) {
			final RequestMonitor monitor = getRequestMonitor();
			
			int flags = 0;
			
			if (monitor != null) {
				flags = flags | monitor.onPreExecute(mContext, request);
			}
			
			final UpdateResult result = super.execute(request);
			
			if (monitor != null) {
				flags = flags | monitor.onPostExecute(mContext, request, result);
			}
			
			return (UpdateResult) appendFlags(result, flags);
		}
		
		@Override
		public InsertResult execute(final Insert request) {
			final RequestMonitor monitor = getRequestMonitor();
			
			int flags = 0;
			
			if (monitor != null) {
				flags = flags | monitor.onPreExecute(mContext, request);
			}
			
			final InsertResult result = super.execute(request);
			
			if (monitor != null) {
				flags = flags | monitor.onPostExecute(mContext, request, result);
			}
			
			return (InsertResult) appendFlags(result, flags);
		}
		
		@Override
		public DeleteResult execute(final Delete request) {
			final RequestMonitor monitor = getRequestMonitor();
			
			int flags = 0;
			
			if (monitor != null) {
				flags = flags | monitor.onPreExecute(mContext, request);
			}
			
			final DeleteResult result = super.execute(request);
			
			if (monitor != null) {
				flags = flags | monitor.onPostExecute(mContext, request, result);
			}
			
			return (DeleteResult) appendFlags(result, flags);
		}


		private static Result<?> appendFlags(final Result<?> result, int flags) {
			result.setIsValid((flags & Flags.DATA_VALID) != 0);
			result.setIsSyncing((flags & Flags.DATA_SYNCING) != 0);
			return result;
		}
		
	}
}
