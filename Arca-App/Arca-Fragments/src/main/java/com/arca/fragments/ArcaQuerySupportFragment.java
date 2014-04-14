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
package com.arca.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.arca.dispatcher.Query;
import com.arca.dispatcher.QueryListener;
import com.arca.monitor.ArcaDispatcher;
import com.arca.monitor.RequestMonitor;

public abstract class ArcaQuerySupportFragment extends Fragment implements QueryListener {

	private ArcaDispatcher mDispatcher;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDispatcher = onCreateDispatcher(savedInstanceState);
	}

	public ArcaDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
		return ArcaDispatcherFactory.generateDispatcher(this);
	}

	protected ArcaDispatcher getRequestDispatcher() {
		return mDispatcher;
	}

	protected RequestMonitor getRequestMonitor() {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			return dispatcher.getRequestMonitor();
		} else {
			return null;
		}
	}

	protected void setRequestMonitor(final RequestMonitor monitor) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			dispatcher.setRequestMonitor(monitor);
		}
	}

	protected void execute(final Query query) {
		final ArcaDispatcher dispatcher = getRequestDispatcher();
		if (dispatcher != null) {
			dispatcher.execute(query, this);
		}
	}
}