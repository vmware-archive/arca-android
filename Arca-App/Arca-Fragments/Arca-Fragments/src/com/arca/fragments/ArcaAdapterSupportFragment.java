package com.arca.fragments;

import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import com.arca.dispatcher.Error;
import com.arca.dispatcher.QueryResult;

public abstract class ArcaAdapterSupportFragment extends ArcaQuerySupportFragment {
	
	public abstract CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState);
	
	private AdapterView<CursorAdapter> mAdapterView;
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAdapterView(view, savedInstanceState);
	}
	
	@SuppressWarnings("unchecked")
	private void setupAdapterView(final View view, final Bundle savedInstanceState) {
		mAdapterView = (AdapterView<CursorAdapter>) view.findViewById(getAdapterViewId());
		mAdapterView.setAdapter(onCreateAdapter(mAdapterView, savedInstanceState));
	}
	
	public int getAdapterViewId() {
		return android.R.id.list;
	}
	
	public AdapterView<CursorAdapter> getAdapterView() {
		return mAdapterView;
	}
	
	public CursorAdapter getCursorAdapter() {
		final AdapterView<CursorAdapter> adapterView = getAdapterView();
		if (adapterView != null) {
			return adapterView.getAdapter();
		} else {
			return null;
		}
	}
	
	@Override
	public void onRequestComplete(final QueryResult result) {
		if (result.hasError()) {
			onContentError(result.getError());
		} else {
			getCursorAdapter().swapCursor(result.getResult());
			onContentChanged(result);
		}
	}
	
	@Override
	public void onRequestReset() {
		getCursorAdapter().swapCursor(null);
		onContentReset();
	}
	
	protected void onContentChanged(final QueryResult result) {}
	protected void onContentError(final Error error) {}
	protected void onContentReset() {}
}
