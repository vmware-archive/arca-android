package io.pivotal.arca.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.pivotal.arca.dispatcher.Batch;
import io.pivotal.arca.dispatcher.BatchResult;
import io.pivotal.arca.dispatcher.Delete;
import io.pivotal.arca.dispatcher.DeleteResult;
import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.Insert;
import io.pivotal.arca.dispatcher.InsertResult;
import io.pivotal.arca.dispatcher.Query;
import io.pivotal.arca.dispatcher.QueryListener;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.dispatcher.Update;
import io.pivotal.arca.dispatcher.UpdateResult;
import io.pivotal.arca.monitor.ArcaDispatcher;
import io.pivotal.arca.monitor.RequestMonitor;

public class ArcaQuerySupportFragment extends Fragment implements QueryListener {

	private ArcaDispatcher mDispatcher;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDispatcher = onCreateDispatcher(savedInstanceState);
	}

	public ArcaDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
		return ArcaDispatcherFactory.generateDispatcher(this);
	}

	protected ArcaDispatcher getRequestDispatcher() {
		return mDispatcher;
	}

	protected RequestMonitor getRequestMonitor() {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			return dispatcher.getRequestMonitor();
		} else {
			return null;
		}
	}

	protected void setRequestMonitor(final RequestMonitor monitor) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			dispatcher.setRequestMonitor(monitor);
		}
	}

	protected void execute(final Query query) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			dispatcher.execute(query, this);
		}
	}

	protected InsertResult execute(final Insert request) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			return dispatcher.execute(request);
		} else {
			return new InsertResult(new Error(0, "No dispatcher found"));
		}
	}

	protected UpdateResult execute(final Update request) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			return dispatcher.execute(request);
		} else {
			return new UpdateResult(new Error(0, "No dispatcher found"));
		}
	}

	protected DeleteResult execute(final Delete request) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			return dispatcher.execute(request);
		} else {
			return new DeleteResult(new Error(0, "No dispatcher found"));
		}
	}

	protected BatchResult execute(final Batch request) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			return dispatcher.execute(request);
		} else {
			return new BatchResult(new Error(0, "No dispatcher found"));
		}
	}

    @Override
    public void onRequestComplete(final QueryResult queryResult) {

    }
}
