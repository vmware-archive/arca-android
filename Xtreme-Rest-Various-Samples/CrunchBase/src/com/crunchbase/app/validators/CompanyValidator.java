package com.crunchbase.app.validators;

import android.content.Context;

import com.xtreme.rest.RestQueryValidator;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;

public class CompanyValidator implements RestQueryValidator {

	@Override
	public ContentState validate(final Query request, final QueryResult result) {
		return ContentState.VALID; 
	}

	@Override
	public boolean refresh(final Context context, final Query request, final QueryResult result) {
		return false;
	}
}
