package com.appnet.app.validators;

import android.content.Context;

import com.appnet.app.operations.PostListOperation;
import com.xtreme.rest.RestQueryValidator;
import com.xtreme.rest.RestService;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;

public class PostListValidator implements RestQueryValidator {
	
	@Override
	public ContentState validate(final Query request, final QueryResult result) {
		return (result.getResult().getCount() > 0) ? ContentState.VALID : ContentState.INVALID; 
	}

	@Override
	public boolean refresh(final Context context, final Query request, final QueryResult result) {
		return RestService.start(context, new PostListOperation(request.getUri()));
	}
}
