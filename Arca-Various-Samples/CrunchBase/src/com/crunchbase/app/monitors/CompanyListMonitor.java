package com.crunchbase.app.monitors;

import android.content.Context;

import com.arca.ArcaService;
import com.arca.dispatcher.Query;
import com.arca.dispatcher.QueryResult;
import com.arca.monitor.RequestMonitor.AbstractRequestMonitor;
import com.crunchbase.app.operations.CompanyListOperation;

public class CompanyListMonitor extends AbstractRequestMonitor {

	@Override
	public int onPostExecute(final Context context, final Query request, final QueryResult result) {
		final int count = result.getResult().getCount();
		if (count == 0 && ArcaService.start(context, new CompanyListOperation(request.getUri()))) {
			return Flags.DATA_SYNCING; 
		} else {
			return Flags.DATA_VALID;
		}
	}
}
