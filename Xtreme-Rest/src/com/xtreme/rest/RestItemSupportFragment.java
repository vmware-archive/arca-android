package com.xtreme.rest;

import android.os.Bundle;
import android.view.View;

import com.xtreme.rest.adapters.ItemCursorAdapter;
import com.xtreme.rest.dispatcher.Error;
import com.xtreme.rest.dispatcher.QueryResult;

/**
 * A {@link RestQuerySupportFragment} that adds convenient support for 
 * a single item by wrapping a {@link ItemCursorAdapter}.
 */
public abstract class RestItemSupportFragment extends RestQuerySupportFragment {

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
