package com.xtreme.rest.monitor;

import android.content.Context;

import com.xtreme.rest.dispatcher.Delete;
import com.xtreme.rest.dispatcher.DeleteResult;
import com.xtreme.rest.dispatcher.Insert;
import com.xtreme.rest.dispatcher.InsertResult;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.dispatcher.Update;
import com.xtreme.rest.dispatcher.UpdateResult;

public interface RequestMonitor { 

	public static interface Flags {
		public static final int DATA_VALID = 1 << 0;
		public static final int DATA_SYNCING = 1 << 1;
	}
	
	public int onPreExecute(final Context context, final Query request);
	
	public int onPostExecute(final Context context, final Query request, final QueryResult result);
	
	public int onPreExecute(final Context context, final Update request);
	
	public int onPostExecute(final Context context, final Update request, final UpdateResult result);
	
	public int onPreExecute(final Context context, final Insert request);
	
	public int onPostExecute(final Context context, final Insert request, final InsertResult result);
	
	public int onPreExecute(final Context context, final Delete request);
	
	public int onPostExecute(final Context context, final Delete request, final DeleteResult result);

	
	public static abstract class AbstractRequestMonitor implements RequestMonitor {

		@Override
		public int onPreExecute(final Context context, final Query request) {
			return Flags.DATA_VALID;
		}

		@Override
		public int onPostExecute(final Context context, final Query request, final QueryResult result) {
			return Flags.DATA_VALID;
		}

		@Override
		public int onPreExecute(final Context context, final Update request) {
			return Flags.DATA_VALID;
		}

		@Override
		public int onPostExecute(final Context context, final Update request, final UpdateResult result) {
			return Flags.DATA_VALID;
		}

		@Override
		public int onPreExecute(final Context context, final Insert request) {
			return Flags.DATA_VALID;
		}

		@Override
		public int onPostExecute(final Context context, final Insert request, final InsertResult result) {
			return Flags.DATA_VALID;
		}

		@Override
		public int onPreExecute(final Context context, final Delete request) {
			return Flags.DATA_VALID;
		}

		@Override
		public int onPostExecute(final Context context, final Delete request, final DeleteResult result) {
			return Flags.DATA_VALID;
		}
		
	}
}
