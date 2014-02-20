package com.crunchbase.app.verifiers;

import android.content.Context;

import com.crunchbase.app.operations.CompanyListOperation;
import com.xtreme.rest.RestService;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.fragments.RestQueryVerifier;

public class CompanyListVerifier implements RestQueryVerifier {

	@Override
	public ContentState verify(final Context context, final Query request, final QueryResult result) {
		return (result.getResult().getCount() > 0) ? ContentState.VALID : ContentState.INVALID; 
	}

	@Override
	public boolean refresh(final Context context, final Query request, final QueryResult result) {
		return RestService.start(context, new CompanyListOperation(request.getUri()));
	}
}
