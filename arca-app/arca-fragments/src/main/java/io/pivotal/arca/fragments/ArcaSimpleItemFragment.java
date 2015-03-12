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

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.Collection;

import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.ModernItemAdapter;
import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.monitor.ArcaDispatcher;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ArcaSimpleItemFragment extends ArcaItemFragment {

    private ArcaViewManager mManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(FragmentUtils.getFragmentLayout(this.getClass()), container, false);
    }

    @Override
    public CursorAdapter onCreateAdapter(final View view, final Bundle savedInstanceState) {
        final Collection<Binding> bindings = FragmentUtils.getBindings(this.getClass());
        final ModernItemAdapter adapter = new ModernItemAdapter(getActivity(), bindings);
        adapter.setViewBinder(FragmentUtils.createViewBinder(this.getClass()));
        return adapter;
    }

    @Override
    public ArcaDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
        final ArcaDispatcher dispatcher = super.onCreateDispatcher(savedInstanceState);
        dispatcher.setRequestMonitor(FragmentUtils.createRequestMonitor(this.getClass()));
        return dispatcher;
    }

    public ArcaViewManager onCreateViewManager(final View view, final Bundle savedInstanceState) {
        return new ArcaViewManager(view);
    }

    @Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setupViewManager(view, savedInstanceState);
	}

    @Override
	public void onStart() {
		super.onStart();

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			setupViewManager(getView(), null);
		}
	}

    protected ArcaViewManager getViewManager() {
        return mManager;
    }

    private void setupViewManager(final View view, final Bundle savedInstanceState) {
        mManager = onCreateViewManager(view, savedInstanceState);
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
