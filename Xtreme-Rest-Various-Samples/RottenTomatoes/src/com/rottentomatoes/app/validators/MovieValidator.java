package com.rottentomatoes.app.validators;

import android.content.Context;

import com.rottentomatoes.app.operations.MovieOperation;
import com.xtreme.rest.RestService;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.validator.ContentState;
import com.xtreme.rest.validator.QueryValidator;

public class MovieValidator implements QueryValidator {
	
	@Override
	public ContentState validate(final Query request, final QueryResult result) {
		return (result.getResult().getCount() > 0) ? ContentState.VALID : ContentState.INVALID; 
	}

	@Override
	public boolean refresh(final Context context, final Query request, final QueryResult result) {
		return RestService.start(context, new MovieOperation(request.getUri()));
	}
}
