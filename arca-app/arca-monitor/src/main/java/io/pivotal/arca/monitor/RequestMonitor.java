/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.monitor;

import android.content.Context;

import io.pivotal.arca.dispatcher.Delete;
import io.pivotal.arca.dispatcher.DeleteResult;
import io.pivotal.arca.dispatcher.Insert;
import io.pivotal.arca.dispatcher.InsertResult;
import io.pivotal.arca.dispatcher.Query;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.dispatcher.Update;
import io.pivotal.arca.dispatcher.UpdateResult;

public interface RequestMonitor {

	public static interface Flags {
        public static final int DATA_VALID = 0;
        public static final int DATA_INVALID = 1 << 0;
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
