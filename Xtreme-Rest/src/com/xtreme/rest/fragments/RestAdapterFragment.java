package com.xtreme.rest.fragments;

import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.dispatcher.Error;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListView;

/**
 * A {@link RestQueryFragment} that adds convenient support for {@link AdapterView}s 
 * such as {@link ListView} or {@link GridView} by wrapping a {@link CursorAdapter}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class RestAdapterFragment extends RestQueryFragment {
	
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
