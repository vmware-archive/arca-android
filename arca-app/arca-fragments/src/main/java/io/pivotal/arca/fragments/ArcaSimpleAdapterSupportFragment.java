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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.Collection;

import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.SupportCursorAdapter;
import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.monitor.ArcaDispatcher;

public class ArcaSimpleAdapterSupportFragment extends ArcaAdapterSupportFragment implements AdapterView.OnItemClickListener {

    private ArcaViewManager mManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(FragmentUtils.getFragmentLayout(this.getClass()), container, false);
        final AbsListView listView = (AbsListView) view.findViewById(getAdapterViewId());
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {

    }

    public CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState) {
        final int layout = FragmentUtils.getAdapterItemLayout(this.getClass());
        final Collection<Binding> bindings = FragmentUtils.getBindings(this.getClass());
        final SupportCursorAdapter adapter = new SupportCursorAdapter(getActivity(), layout, bindings);
        adapter.setViewBinder(FragmentUtils.createViewBinder(this.getClass()));
        return adapter;
    }

    @Override
    public ArcaDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
        final ArcaDispatcher dispatcher = super.onCreateDispatcher(savedInstanceState);
        dispatcher.setRequestMonitor(FragmentUtils.createRequestMonitor(this.getClass()));
        return dispatcher;
    }

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        setupViewManager(view);
	}

    protected ArcaViewManager getViewManager() {
        return mManager;
    }

    private void setupViewManager(final View view) {
        mManager = new ArcaViewManager(view);
        mManager.showProgressView();
    }

    @Override
    public void onContentChanged(final QueryResult result) {
        mManager.checkResult(result);
    }

    @Override
    public void onContentError(final Error error) {
        mManager.checkError(error);
    }
}
