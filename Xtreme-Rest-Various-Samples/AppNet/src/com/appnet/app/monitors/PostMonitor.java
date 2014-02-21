package com.appnet.app.monitors;

import android.content.Context;

import com.appnet.app.operations.PostOperation;
import com.xtreme.rest.RestService;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.monitor.RequestMonitor.AbstractRequestMonitor;

public class PostMonitor extends AbstractRequestMonitor {

	@Override
	public int onPostExecute(final Context context, final Query request, final QueryResult result) {
		final int count = result.getResult().getCount();
		if (count == 0 && RestService.start(context, new PostOperation(request.getUri()))) {
			return Flags.DATA_SYNCING; 
		} else {
			return Flags.DATA_VALID;
		}
	}
}
