/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;

import io.pivotal.arca.dispatcher.QueryResult;

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
			getCursorAdapter().swapCursor(result.getData());
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

	protected void onContentChanged(final QueryResult result) {
	}

	protected void onContentError(final io.pivotal.arca.dispatcher.Error error) {
	}

	protected void onContentReset() {
	}
}
