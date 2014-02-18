package com.xtreme.rest;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;

import android.content.Context;

public interface RestQueryValidator {

	public static enum ContentState {
		VALID, INVALID, EXPIRED
	}

	public ContentState validate(final Query request, final QueryResult result);

	public boolean refresh(final Context context, final Query request, final QueryResult result);
}
