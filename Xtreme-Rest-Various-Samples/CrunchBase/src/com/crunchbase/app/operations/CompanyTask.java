package com.crunchbase.app.operations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.crunchbase.app.datasets.CompanyTable;
import com.crunchbase.app.models.Company;
import com.crunchbase.app.providers.CrunchBaseContentProvider;
import com.google.gson.Gson;
import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public class CompanyTask extends Task<String> {

	private final String mId;
	
	public CompanyTask(final String id) {
		mId = id;
	}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		// TODO this needs to be a unique identifier across all tasks
		return new RequestIdentifier<String>(String.format("company::%s", mId));
	}
	
	@Override
	public String onExecuteNetworkRequest(final Context context) throws Exception {
		// TODO make network request to fetch object(s) 
		// return CrunchBaseRequests.getCompany(mId);
		throw new Exception("Override this method to return a json string for a Company.");
	}

	@Override
	public void onExecuteProcessingRequest(final Context context, final String data) throws Exception {
		// TODO parse the response and insert into content provider
		final Company item = new Gson().fromJson(data, Company.class);
		final ContentValues values = CompanyTable.getInstance().getContentValues(item);
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(CrunchBaseContentProvider.Uris.COMPANIES_URI, values);
	}
}
