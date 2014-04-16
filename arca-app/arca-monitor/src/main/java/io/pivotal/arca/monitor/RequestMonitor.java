/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
			return 0;
		}

		@Override
		public int onPostExecute(final Context context, final Query request, final QueryResult result) {
			return 0;
		}

		@Override
		public int onPreExecute(final Context context, final Update request) {
			return 0;
		}

		@Override
		public int onPostExecute(final Context context, final Update request, final UpdateResult result) {
			return 0;
		}

		@Override
		public int onPreExecute(final Context context, final Insert request) {
			return 0;
		}

		@Override
		public int onPostExecute(final Context context, final Insert request, final InsertResult result) {
			return 0;
		}

		@Override
		public int onPreExecute(final Context context, final Delete request) {
			return 0;
		}

		@Override
		public int onPostExecute(final Context context, final Delete request, final DeleteResult result) {
			return 0;
		}

	}
}
