package io.pivotal.arca.fragments;

import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.QueryResult;

public abstract class ArcaAdapterSupportFragment extends ArcaQuerySupportFragment {

	public abstract CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState);

    private AdapterView<CursorAdapter> mAdapterView;
    private CursorAdapter mAdapter;

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setupAdapterView(view, savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	private void setupAdapterView(final View view, final Bundle savedInstanceState) {
        mAdapterView = (AdapterView<CursorAdapter>) view.findViewById(getAdapterViewId());

        mAdapter = onCreateAdapter(mAdapterView, savedInstanceState);

        mAdapterView.setAdapter(mAdapter);
	}

	public int getAdapterViewId() {
		return android.R.id.list;
	}

	public AdapterView<CursorAdapter> getAdapterView() {
		return mAdapterView;
	}

	public CursorAdapter getCursorAdapter() {
		return mAdapter;
	}

	@Override
	public void onRequestComplete(final QueryResult result) {
		if (result.hasError()) {
			onContentError(result.getError());
		} else {
			getCursorAdapter().swapCursor(result.getData());
			onContentChanged(result);
		}
	}

    protected void onContentChanged(final QueryResult result) {
    }

    protected void onContentError(final Error error) {
    }
}
