package com.xtreme.rest.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.xtreme.rest.adapters.ItemCursorAdapter;
import com.xtreme.rest.binders.ViewBinder;
import com.xtreme.rest.loader.ContentResponse;

/**
 * A {@link ContentLoaderFragment} that adds convenient support a single item by wrapping a {@link ItemCursorAdapter}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class ContentLoaderItemFragment extends ContentLoaderFragment implements ViewBinder {

	protected abstract int[] getViewIds();
	protected abstract String[] getColumnNames();

	private ItemCursorAdapter mAdapter;

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAdapterView(view);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			setupAdapterView(getView());
		}
	}
	
	private void setupAdapterView(final View view) {
		final int[] viewIds = getViewIds();
		final String[] columnNames = getColumnNames();
		
		mAdapter = new ItemCursorAdapter(view, columnNames, viewIds);
		mAdapter.setViewBinder(this);
	}
	
	public ItemCursorAdapter getItemAdapter() {
		return mAdapter;
	}
	
	@Override
	public final void onLoaderFinished(final ContentResponse response) {
		mAdapter.swapCursor(response.getCursor());
		onContentChanged(response);
	}
	
	@Override
	public final void onLoaderReset() {
		mAdapter.swapCursor(null);
		onContentReset();
	}
	
	protected void onContentChanged(final ContentResponse response) {}
	protected void onContentReset() {}

}
