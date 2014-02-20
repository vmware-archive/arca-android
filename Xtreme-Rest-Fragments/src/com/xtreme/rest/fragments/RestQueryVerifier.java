package com.xtreme.rest.fragments;

import android.content.Context;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;

public interface RestQueryVerifier {

	public static enum ContentState {
		VALID, INVALID, EXPIRED
	}

	public ContentState verify(final Context context, final Query request, final QueryResult result);

	public boolean refresh(final Context context, final Query request, final QueryResult result);
}
