package com.arca.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.arca.adapters.ItemCursorAdapter;
import com.arca.dispatcher.QueryResult;
import com.arca.dispatcher.Error;

/**
 * A {@link ArcaQueryFragment} that adds convenient support for 
 * a single item by wrapping a {@link ItemCursorAdapter}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class ArcaItemFragment extends ArcaQueryFragment {

	public abstract ItemCursorAdapter onCreateAdapter(final View view, final Bundle savedInstanceState);

	private ItemCursorAdapter mAdapter;

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAdapterView(view, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			setupAdapterView(getView(), null);
		}
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
