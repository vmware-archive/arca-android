package com.xtreme.rest.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.xtreme.rest.adapters.SupportCursorAdapter;
import com.xtreme.rest.binders.ViewBinder;
import com.xtreme.rest.loader.ContentLoader;
import com.xtreme.rest.loader.ContentResponse;

/**
 * A {@link ContentLoaderFragment} that adds convenient support for {@link AdapterView}s such as {@link ListView} or {@link GridView}
 * by wrapping a {@link CursorAdapter}.
 */
public abstract class ContentLoaderAdapterSupportFragment extends ContentLoaderSupportFragment implements ViewBinder {
	
	protected abstract int[] getViewIds();
	protected abstract String[] getColumnNames();
	protected abstract int getAdapterItemResourceId();
	protected abstract int getAdapterViewId();
	
	private AdapterView<CursorAdapter> mAdapterView;
	private CursorAdapter mAdapter;
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAdapterView(view);
	}
	
	@SuppressWarnings("unchecked")
	private void setupAdapterView(final View view) {
		final int[] viewIds = getViewIds();
		final String[] columnNames = getColumnNames();
		final int resourceId = getAdapterItemResourceId();
		final int adapterViewId = getAdapterViewId();
		
		mAdapter = onCreateAdapter(resourceId, viewIds, columnNames);

		mAdapterView = (AdapterView<CursorAdapter>) view.findViewById(adapterViewId);
		mAdapterView.setAdapter(mAdapter);
	}
	
	public CursorAdapter onCreateAdapter(final int itemResourceId, final int[] viewIds, final String[] columnNames) {
		final SupportCursorAdapter adapter = new SupportCursorAdapter(getActivity(), itemResourceId, columnNames, viewIds);
		adapter.setViewBinder(this);
		return adapter;
	}
	
	public AdapterView<?> getAdapterView() {
		return mAdapterView;
	}
	
	public CursorAdapter getCursorAdapter() {
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

	/**
	 * A callback for when the content is changed, usually from a call to {@link #onLoaderFinished(ContentResponse)}.
	 */
	protected void onContentChanged(final ContentResponse response) {}

	/**
	 * A callback for when the {@link ContentLoader} is reset, usually from a call to {@link #onLoaderReset()}.
	 */
	protected void onContentReset() {}
	
	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
		return false;
	}
}
