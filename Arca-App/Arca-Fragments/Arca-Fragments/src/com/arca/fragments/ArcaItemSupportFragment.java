package com.arca.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;

import com.arca.dispatcher.Error;
import com.arca.dispatcher.QueryResult;

/**
 * A {@link ArcaQuerySupportFragment} that adds convenient support for 
 * a single item by wrapping a {@link CursorAdapter}.
 */
public abstract class ArcaItemSupportFragment extends ArcaQuerySupportFragment {

	public abstract CursorAdapter onCreateAdapter(final View view, final Bundle savedInstanceState);

	private CursorAdapter mAdapter;

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAdapterView(view, savedInstanceState);
	}
	
	private void setupAdapterView(final View view, final Bundle savedInstanceState) {
		mAdapter = onCreateAdapter(view, savedInstanceState);
	}
	
	public CursorAdapter getCursorAdapter() {
		return mAdapter;
	}

	@Override
	public final void onRequestComplete(final QueryResult result) {
		if (result.hasError()) {
			onContentError(result.getError());
		} else {
			getCursorAdapter().swapCursor(result.getResult());
			bindViewAtPosition(0);
			onContentChanged(result);
		}
	}

	public void bindViewAtPosition(final int position) {
		final CursorAdapter adapter = getCursorAdapter();
		final Cursor cursor = adapter.getCursor();
		if (cursor != null && cursor.moveToPosition(position)) {
			adapter.bindView(getView(), getActivity(), cursor);
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
