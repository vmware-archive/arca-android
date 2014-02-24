package com.arca.monitor;

import android.content.Context;

import com.arca.dispatcher.Delete;
import com.arca.dispatcher.DeleteResult;
import com.arca.dispatcher.Insert;
import com.arca.dispatcher.InsertResult;
import com.arca.dispatcher.Query;
import com.arca.dispatcher.QueryResult;
import com.arca.dispatcher.Update;
import com.arca.dispatcher.UpdateResult;

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
