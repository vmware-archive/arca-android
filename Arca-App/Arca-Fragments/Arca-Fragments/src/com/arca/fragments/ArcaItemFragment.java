package com.arca.fragments;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;

import com.arca.dispatcher.Error;
import com.arca.dispatcher.QueryResult;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class ArcaItemFragment extends ArcaQueryFragment {

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
