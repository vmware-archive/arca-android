package io.pivotal.arca.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

import java.util.Collection;

import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.ModernCursorAdapter;
import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.monitor.ArcaDispatcher;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ArcaSimpleAdapterFragment extends ArcaAdapterFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ArcaViewManager mManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(FragmentUtils.getFragmentLayout(this.getClass()), container, false);
        final AbsListView listView = (AbsListView) view.findViewById(getAdapterViewId());
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
        return false;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {

    }

    public CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState) {
        final int layout = FragmentUtils.getAdapterItemLayout(this.getClass());
        final Collection<Binding> bindings = FragmentUtils.getBindings(this.getClass());
        final ModernCursorAdapter adapter = new ModernCursorAdapter(getActivity(), layout, bindings);
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
