package com.xtreme.rest.validator;

import android.content.Context;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;

public interface QueryValidator {
	public ContentState validate(final Query request, final QueryResult result);
	public boolean refresh(final Context context, final Query request, final QueryResult result);
}
