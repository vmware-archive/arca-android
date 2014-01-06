package com.xtreme.rest.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.xtreme.rest.loader.ContentLoader;
import com.xtreme.rest.loader.ContentResponse;

/**
 * A {@link ContentLoaderFragment} that adds convenient support for {@link AdapterView}s such as {@link ListView} or {@link GridView}
 * by wrapping a {@link CursorAdapter}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class ContentLoaderAdapterFragment extends ContentLoaderFragment {
	
	protected abstract int getAdapterViewId();
	public abstract CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView);
	
	private AdapterView<CursorAdapter> mAdapterView;
	
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
