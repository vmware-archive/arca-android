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
package com.arca.dispatcher;

import android.os.Bundle;

public interface RequestDispatcher extends RequestExecutor {

	public void execute(Query request, QueryListener listener);

	public void execute(Update request, UpdateListener listener);

	public void execute(Insert request, InsertListener listener);

	public void execute(Delete request, DeleteListener listener);

	public static abstract class AbstractRequestDispatcher implements RequestDispatcher {

		protected static interface Extras {
			public static final String REQUEST = "request";
		}

		private final RequestExecutor mRequestExecutor;

		public AbstractRequestDispatcher(final RequestExecutor executor) {
			mRequestExecutor = executor;
		}

		public RequestExecutor getRequestExecutor() {
			return mRequestExecutor;
		}

		@Override
		public QueryResult execute(final Query request) {
			return getRequestExecutor().execute(request);
		}

		@Override
		public UpdateResult execute(final Update request) {
			return getRequestExecutor().execute(request);
		}

		@Override
		public InsertResult execute(final Insert request) {
			return getRequestExecutor().execute(request);
		}

		@Override
		public DeleteResult execute(final Delete request) {
			return getRequestExecutor().execute(request);
		}

		protected static Bundle createRequestBundle(final Request<?> request) {
			final Bundle bundle = new Bundle();
			bundle.putParcelable(Extras.REQUEST, request);
			return bundle;
		}
	}
}
