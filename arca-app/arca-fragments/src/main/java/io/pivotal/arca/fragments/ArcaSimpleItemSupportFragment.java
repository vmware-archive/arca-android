package io.pivotal.arca.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.SupportItemAdapter;
import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.monitor.ArcaDispatcher;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ArcaSimpleItemSupportFragment extends ArcaItemSupportFragment {

    private ArcaViewManager mManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(FragmentUtils.getFragmentLayout(this.getClass()), container, false);
    }

    @Override
    public CursorAdapter onCreateAdapter(final View view, final Bundle savedInstanceState) {
        final Collection<Binding> bindings = FragmentUtils.getBindings(this.getClass());
        final SupportItemAdapter adapter = new SupportItemAdapter(getActivity(), bindings);
        adapter.setViewBinder(FragmentUtils.createViewBinder(this.getClass()));
        return adapter;
    }

    @Override
    public ArcaDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
        final ArcaDispatcher dispatcher = super.onCreateDispatcher(savedInstanceState);
        dispatcher.setRequestMonitor(FragmentUtils.createRequestMonitor(this.getClass()));
        return dispatcher;
    }

    public ArcaViewManager onCreateViewManager(final View view, final Bundle savedInstanceState) {
        return new ArcaViewManager(view);
    }

    @Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setupViewManager(view, savedInstanceState);
	}

    @Override
	public void onStart() {
		super.onStart();

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			setupViewManager(getView(), null);
		}
	}

    protected ArcaViewManager getViewManager() {
        return mManager;
    }

    private void setupViewManager(final View view, final Bundle savedInstanceState) {
        mManager = onCreateViewManager(view, savedInstanceState);
        mManager.showProgressView();
    }

    @Override
    public void onContentChanged(final QueryResult result) {
        mManager.checkResult(result);
    }

    @Override
    public void onContentError(final Error error) {
        mManager.checkError(error);
    }
}
