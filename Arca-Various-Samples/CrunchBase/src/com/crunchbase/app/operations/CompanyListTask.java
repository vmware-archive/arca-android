package com.crunchbase.app.operations;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.arca.service.Task;
import com.crunchbase.app.application.CrunchBaseRequests;
import com.crunchbase.app.datasets.CompanyTable;
import com.crunchbase.app.models.Company;
import com.crunchbase.app.models.SearchResponse;
import com.crunchbase.app.providers.CrunchBaseContentProvider;
import com.xtreme.threading.RequestIdentifier;

public class CompanyListTask extends Task<List<Company>> {

	private static final int PAGE_SIZE = 30;
	
	private final int mPage;
	private SearchResponse mResponse;

	public CompanyListTask(final int page) {
		mPage = page;
	}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		return new RequestIdentifier<String>("company_list:" + mPage);
	}
	
	@Override
	public List<Company> onExecuteNetworking(final Context context) throws Exception {
		mResponse = CrunchBaseRequests.getSearchResults("toronto", mPage);
		return mResponse.getResults();
	}

	@Override
	public void onExecuteProcessing(final Context context, final List<Company> data) throws Exception {
		final ContentValues[] values = CompanyTable.getContentValues(data);
		final ContentResolver resolver = context.getContentResolver();
		resolver.bulkInsert(CrunchBaseContentProvider.Uris.COMPANIES_URI, values);
	}

	public int getNextPage() {
		final int total = mResponse.getTotal();
		final int page = mResponse.getPage();
		return (PAGE_SIZE * page) < total ? page + 1 : -1;
	}
}
