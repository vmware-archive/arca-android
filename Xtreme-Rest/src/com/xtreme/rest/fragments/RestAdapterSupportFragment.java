package com.xtreme.rest.fragments;

import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.xtreme.rest.loader.ContentLoader;
import com.xtreme.rest.loader.ContentResponse;

/**
 * A {@link RestFragment} that adds convenient support for {@link AdapterView}s such as {@link ListView} or {@link GridView}
 * by wrapping a {@link CursorAdapter}.
 */
public abstract class RestAdapterSupportFragment extends RestSupportFragment {
	
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
	public final void onLoaderFinished(final ContentResponse response) {
		getCursorAdapter().swapCursor(response.getCursor());
		onContentChanged(response);
	}

	@Override
	public final void onLoaderReset() {
		getCursorAdapter().swapCursor(null);
		onContentReset();
	}
	
	/**
	 * A callback for when the content is changed, usually from a call to {@link #onLoaderFinished(ContentResponse)}.
	 */
	protected void onContentChanged(final ContentResponse response) {
		
	}
	
	/**
	 * A callback for when the {@link ContentLoader} is reset, usually from a call to {@link #onLoaderReset()}.
	 */
	protected void onContentReset() {
		
	}
}
