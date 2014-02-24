package com.arca.monitor;

import android.content.ContentResolver;
import android.content.Context;

import com.arca.dispatcher.Delete;
import com.arca.dispatcher.DeleteResult;
import com.arca.dispatcher.Insert;
import com.arca.dispatcher.InsertResult;
import com.arca.dispatcher.Query;
import com.arca.dispatcher.QueryResult;
import com.arca.dispatcher.RequestExecutor;
import com.arca.dispatcher.Result;
import com.arca.dispatcher.Update;
import com.arca.dispatcher.UpdateResult;
import com.arca.monitor.RequestMonitor.Flags;

public interface ArcaExecutor extends RequestExecutor {
	
	public void setRequestMonitor(final RequestMonitor monitor);
	
	public RequestMonitor getRequestMonitor();
	
	public static class DefaultArcaExecutor extends DefaultRequestExecutor implements ArcaExecutor {

		private RequestMonitor mMonitor;
		private final Context mContext;

		public DefaultArcaExecutor(final ContentResolver resolver, final Context context) {
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
			
			int flags = Flags.DATA_VALID;
			
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
			
			int flags = Flags.DATA_VALID;
			
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
			
			int flags = Flags.DATA_VALID;
			
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
			
			int flags = Flags.DATA_VALID;
			
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
