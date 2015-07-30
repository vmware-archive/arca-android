/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.fragments;

import android.os.Bundle;
import android.view.View;

import io.pivotal.arca.adapters.RecyclerViewCursorAdapter;
import io.pivotal.arca.dispatcher.QueryResult;

public abstract class ArcaItemRecyclerViewFragment extends ArcaQuerySupportFragment {

	public abstract RecyclerViewCursorAdapter onCreateAdapter(final View view, final Bundle savedInstanceState);

	private RecyclerViewCursorAdapter mAdapter;

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setupAdapterView(view, savedInstanceState);
	}

	private void setupAdapterView(final View view, final Bundle savedInstanceState) {
		mAdapter = onCreateAdapter(view, savedInstanceState);
	}

	public RecyclerViewCursorAdapter getCursorAdapter() {
		return mAdapter;
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

	protected void onContentChanged(final QueryResult result) {
	}

	protected void onContentError(final io.pivotal.arca.dispatcher.Error error) {
	}

	protected void onContentReset() {
	}
}