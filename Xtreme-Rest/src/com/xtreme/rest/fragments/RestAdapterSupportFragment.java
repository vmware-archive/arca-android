package com.xtreme.rest.fragments;

import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.dispatcher.Error;

/**
 * A {@link RestQuerySupportFragment} that adds convenient support for {@link AdapterView}s 
 * such as {@link ListView} or {@link GridView} by wrapping a {@link CursorAdapter}.
 */
public abstract class RestAdapterSupportFragment extends RestQuerySupportFragment {
	
	protected abstract int getAdapterViewId();
	public abstract CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView);
	
	private AdapterView<CursorAdapter> mAdapterView;
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAdapterView(view);
	}
	
	@SuppressWarnings("unchecked")
	private void setupAdapterView(final View view) {
		final int adapterViewId = getAdapterViewId();		
		mAdapterView = (AdapterView<CursorAdapter>) view.findViewById(adapterViewId);
		mAdapterView.setAdapter(onCreateAdapter(mAdapterView));
	}
	
	public AdapterView<CursorAdapter> getAdapterView() {
		return mAdapterView;
	}
	
	public CursorAdapter getCursorAdapter() {
		return getAdapterView().getAdapter();
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
