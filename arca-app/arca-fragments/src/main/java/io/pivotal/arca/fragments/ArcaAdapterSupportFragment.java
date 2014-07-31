/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.fragments;

import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.QueryResult;

public abstract class ArcaAdapterSupportFragment extends ArcaQuerySupportFragment {

	public abstract CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState);

	private AdapterView<CursorAdapter> mAdapterView;
    private CursorAdapter mAdapter;

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setupAdapterView(view, savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	private void setupAdapterView(final View view, final Bundle savedInstanceState) {
        mAdapter = onCreateAdapter(mAdapterView, savedInstanceState);

		mAdapterView = (AdapterView<CursorAdapter>) view.findViewById(getAdapterViewId());
		mAdapterView.setAdapter(mAdapter);
	}

	public int getAdapterViewId() {
		return android.R.id.list;
	}

	public AdapterView<CursorAdapter> getAdapterView() {
		return mAdapterView;
	}

	public CursorAdapter getCursorAdapter() {
		return mAdapter;
	}

	@Override
	public void onRequestComplete(final QueryResult result) {
		if (result.hasError()) {
			onContentError(result.getError());
		} else {
			getCursorAdapter().swapCursor(result.getResult());
			onContentChanged(result);
		}
	}

	@Override
	public void onRequestReset() {
		getCursorAdapter().swapCursor(null);
		onContentReset();
	}

	protected void onContentChanged(final QueryResult result) {
	}

	protected void onContentError(final Error error) {
	}

	protected void onContentReset() {
	}
}
