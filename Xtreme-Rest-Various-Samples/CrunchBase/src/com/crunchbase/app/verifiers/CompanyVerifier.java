package com.crunchbase.app.verifiers;

import android.content.Context;

import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.fragments.RestQueryVerifier;

public class CompanyVerifier implements RestQueryVerifier {

	@Override
	public ContentState verify(final Context context, final Query request, final QueryResult result) {
		return ContentState.VALID; 
	}

	@Override
	public boolean refresh(final Context context, final Query request, final QueryResult result) {
		return false;
	}
}
