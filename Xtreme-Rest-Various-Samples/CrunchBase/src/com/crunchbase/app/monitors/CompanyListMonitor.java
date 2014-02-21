package com.crunchbase.app.monitors;

import android.content.Context;

import com.crunchbase.app.operations.CompanyListOperation;
import com.xtreme.rest.RestService;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.monitor.RequestMonitor.AbstractRequestMonitor;

public class CompanyListMonitor extends AbstractRequestMonitor {

	@Override
	public int onPostExecute(final Context context, final Query request, final QueryResult result) {
		final int count = result.getResult().getCount();
		if (count == 0 && RestService.start(context, new CompanyListOperation(request.getUri()))) {
			return Flags.DATA_SYNCING; 
		} else {
			return Flags.DATA_VALID;
		}
	}
}
