package com.xtreme.rest.utils;

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
