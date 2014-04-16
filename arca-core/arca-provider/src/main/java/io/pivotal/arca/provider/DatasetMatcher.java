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
package io.pivotal.arca.provider;

import android.content.UriMatcher;
import android.net.Uri;

import java.util.Collection;

public interface DatasetMatcher {

	public void register(final String authority, final String path, final Class<? extends Dataset> datasetClass);

	public Dataset matchDataset(final Uri uri);

	public Collection<Dataset> getDatasets();

	public static class DefaultMatcher implements DatasetMatcher {

		private int MATCH = 0;
		private final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		private final ClassMapping<Dataset> mDatasetMapping = new ClassMapping<Dataset>();

		@Override
		public void register(final String authority, final String path, final Class<? extends Dataset> klass) {
			final int match = MATCH++;
			mUriMatcher.addURI(authority, path, match);
			mDatasetMapping.append(match, klass);
		}

		@Override
		public Dataset matchDataset(final Uri uri) {
			final int match = mUriMatcher.match(uri);
			final Dataset dataset = mDatasetMapping.get(match);
			return dataset;
		}

		@Override
		public Collection<Dataset> getDatasets() {
			return mDatasetMapping.values();
		}
	}
}
