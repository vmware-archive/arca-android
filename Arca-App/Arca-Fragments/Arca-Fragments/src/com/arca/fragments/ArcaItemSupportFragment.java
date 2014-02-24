package com.arca.fragments;

import android.os.Bundle;
import android.view.View;

import com.arca.adapters.ItemCursorAdapter;
import com.arca.dispatcher.QueryResult;
import com.arca.dispatcher.Error;

/**
 * A {@link ArcaQuerySupportFragment} that adds convenient support for 
 * a single item by wrapping a {@link ItemCursorAdapter}.
 */
public abstract class ArcaItemSupportFragment extends ArcaQuerySupportFragment {

	public abstract ItemCursorAdapter onCreateAdapter(final View view, final Bundle savedInstanceState);

	private ItemCursorAdapter mAdapter;

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAdapterView(view, savedInstanceState);
	}
	
	private void setupAdapterView(final View view, final Bundle savedInstanceState) {
		mAdapter = onCreateAdapter(view, savedInstanceState);
	}
	
	public ItemCursorAdapter getItemAdapter() {
		return mAdapter;
	}
	
	@Override
	public final void onRequestComplete(final QueryResult result) {
		if (result.hasError()) {
			onContentError(result.getError());
		} else {
			getItemAdapter().swapCursor(result.getResult());
			onContentChanged(result);
		}
	}
	
	@Override
	public final void onRequestReset() {
		getItemAdapter().swapCursor(null);
		onContentReset();
	}
	
	protected void onContentChanged(final QueryResult result) {}
	protected void onContentError(final Error error) {}
	protected void onContentReset() {}
}
