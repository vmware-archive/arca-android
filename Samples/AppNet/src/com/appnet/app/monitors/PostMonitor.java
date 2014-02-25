package com.appnet.app.monitors;

import android.content.Context;

import com.appnet.app.operations.PostOperation;
import com.arca.ArcaService;
import com.arca.dispatcher.Query;
import com.arca.dispatcher.QueryResult;
import com.arca.monitor.RequestMonitor.AbstractRequestMonitor;

public class PostMonitor extends AbstractRequestMonitor {

	@Override
	public int onPostExecute(final Context context, final Query request, final QueryResult result) {
		final int count = result.getResult().getCount();
		if (count == 0 && ArcaService.start(context, new PostOperation(request.getUri()))) {
			return Flags.DATA_SYNCING; 
		} else {
			return 0;
		}
	}
}
