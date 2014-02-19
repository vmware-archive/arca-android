package com.xtreme.rest;

import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.xtreme.rest.dispatcher.Error;
import com.xtreme.rest.dispatcher.QueryResult;

/**
 * A {@link RestQuerySupportFragment} that adds convenient support for {@link AdapterView}s 
 * such as {@link ListView} or {@link GridView} by wrapping a {@link CursorAdapter}.
 */
public abstract class RestAdapterSupportFragment extends RestQuerySupportFragment {
	
	protected abstract int getAdapterViewId();
	
	public abstract CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState);
	
	private AdapterView<CursorAdapter> mAdapterView;
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAdapterView(view, savedInstanceState);
	}
	
	@SuppressWarnings("unchecked")
	private void setupAdapterView(final View view, final Bundle savedInstanceState) {
		final int adapterViewId = getAdapterViewId();		
		mAdapterView = (AdapterView<CursorAdapter>) view.findViewById(adapterViewId);
		mAdapterView.setAdapter(onCreateAdapter(mAdapterView, savedInstanceState));
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
	public final void onRequestComplete(final QueryResult result) {
		if (result.hasError()) {
			onContentError(result.getError());
		} else {
			getCursorAdapter().swapCursor(result.getResult());
			onContentChanged(result);
		}
	}
	
	@Override
	public final void onRequestReset() {
		getCursorAdapter().swapCursor(null);
		onContentReset();
	}
	
	protected void onContentChanged(final QueryResult result) {}
	protected void onContentError(final Error error) {}
	protected void onContentReset() {}
}
