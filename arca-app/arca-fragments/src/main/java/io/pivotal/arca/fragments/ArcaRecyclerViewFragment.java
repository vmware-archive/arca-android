package io.pivotal.arca.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.pivotal.arca.adapters.RecyclerViewCursorAdapter;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.dispatcher.Error;

public abstract class ArcaRecyclerViewFragment extends ArcaQueryFragment {

	public abstract RecyclerViewCursorAdapter onCreateAdapter(final RecyclerView recyclerView, final Bundle savedInstanceState);

	private RecyclerView mRecyclerView;
	private RecyclerViewCursorAdapter mAdapter;
	private RecyclerView.LayoutManager mManager;

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setupRecyclerView(view, savedInstanceState);
	}

	public RecyclerView.LayoutManager onCreateLayoutManager(final RecyclerView recyclerView, final Bundle savedInstanceState) {
		return new LinearLayoutManager(getActivity());
	}

	private void setupRecyclerView(final View view, final Bundle savedInstanceState) {
		mRecyclerView = (RecyclerView) view.findViewById(getRecyclerViewId());

		mManager = onCreateLayoutManager(mRecyclerView, savedInstanceState);
		mAdapter = onCreateAdapter(mRecyclerView, savedInstanceState);

		mRecyclerView.setLayoutManager(mManager);
		mRecyclerView.setAdapter(mAdapter);
	}

	public int getRecyclerViewId() {
		return android.R.id.list;
	}

	public RecyclerView getRecyclerView() {
		return mRecyclerView;
	}

	public RecyclerViewCursorAdapter getRecyclerViewAdapter() {
		return mAdapter;
	}

	@Override
	public final void onRequestComplete(final QueryResult result) {
		if (result.hasError()) {
			onContentError(result.getError());
		} else {
			getRecyclerViewAdapter().swapCursor(result.getData());
			onContentChanged(result);
		}
	}

	protected void onContentChanged(final QueryResult result) {
	}

	protected void onContentError(final Error error) {
	}
}
