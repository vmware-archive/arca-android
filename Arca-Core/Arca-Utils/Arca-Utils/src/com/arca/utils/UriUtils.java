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
package com.arca.utils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import android.net.Uri;

public class UriUtils {

	public static Uri stripQueryParameter(final Uri uri, final String removeParam) {
		if (uri.getQueryParameter(removeParam) != null) {
			return removeQueryParameter(uri, removeParam);
		} else {
			return uri;
		}
	}

	private static Uri removeQueryParameter(final Uri uri, final String removeParam) {
		final String baseUri = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
		final Uri.Builder builder = Uri.parse(baseUri).buildUpon();
		final Set<String> params = getQueryParameterNames(uri);
		for (final String param : params) {
			if (!param.equals(removeParam)) {
				final String value = uri.getQueryParameter(param);
				builder.appendQueryParameter(param, value);
			}
		}
		return builder.build();
	}

	public static Set<String> getQueryParameterNames(final Uri uri) {
		if (uri.isOpaque()) {
			throw new UnsupportedOperationException("This isn't a hierarchical URI.");
		}

		final String query = uri.getEncodedQuery();
		if (query == null) {
			return Collections.emptySet();
		}

		final Set<String> names = new LinkedHashSet<String>();
		int start = 0;
		do {
			final int next = query.indexOf('&', start);
			final int end = (next == -1) ? query.length() : next;

			int separator = query.indexOf('=', start);
			if (separator > end || separator == -1) {
				separator = end;
			}

			final String name = query.substring(start, separator);
			names.add(Uri.decode(name));

			// Move start to end of name.
			start = end + 1;
		} while (start < query.length());

		return Collections.unmodifiableSet(names);
	}

}
