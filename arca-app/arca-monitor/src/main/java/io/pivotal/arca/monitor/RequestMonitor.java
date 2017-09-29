package io.pivotal.arca.monitor;

import android.content.Context;

import io.pivotal.arca.dispatcher.Batch;
import io.pivotal.arca.dispatcher.BatchResult;
import io.pivotal.arca.dispatcher.Delete;
import io.pivotal.arca.dispatcher.DeleteResult;
import io.pivotal.arca.dispatcher.Insert;
import io.pivotal.arca.dispatcher.InsertResult;
import io.pivotal.arca.dispatcher.Query;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.dispatcher.Update;
import io.pivotal.arca.dispatcher.UpdateResult;

public interface RequestMonitor {

	interface Flags {
        int DATA_VALID = 0;
        int DATA_INVALID = 1 << 0;
		int DATA_SYNCING = 1 << 1;
	}

	int onPreExecute(final Context context, final Query request);
	int onPostExecute(final Context context, final Query request, final QueryResult result);

	int onPreExecute(final Context context, final Update request);
	int onPostExecute(final Context context, final Update request, final UpdateResult result);

	int onPreExecute(final Context context, final Insert request);
	int onPostExecute(final Context context, final Insert request, final InsertResult result);

	int onPreExecute(final Context context, final Delete request);
	int onPostExecute(final Context context, final Delete request, final DeleteResult result);

	int onPreExecute(final Context context, final Batch request);
	int onPostExecute(final Context context, final Batch request, final BatchResult result);

	abstract class AbstractRequestMonitor implements RequestMonitor {

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

		@Override
		public int onPreExecute(final Context context, final Batch request) {
			return Flags.DATA_VALID;
		}

		@Override
		public int onPostExecute(final Context context, final Batch request, final BatchResult result) {
			return Flags.DATA_VALID;
		}
	}
}
