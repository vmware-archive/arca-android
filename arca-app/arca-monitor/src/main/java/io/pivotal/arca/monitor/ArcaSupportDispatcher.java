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
package io.pivotal.arca.monitor;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import io.pivotal.arca.dispatcher.SupportRequestDispatcher;

public class ArcaSupportDispatcher extends SupportRequestDispatcher implements ArcaDispatcher {

	public ArcaSupportDispatcher(final ArcaExecutor executor, final Context context, final LoaderManager manager) {
		super(executor, context, manager);
	}

	@Override
	public void setRequestMonitor(final RequestMonitor monitor) {
		final ArcaExecutor executor = (ArcaExecutor) getRequestExecutor();
		executor.setRequestMonitor(monitor);
	}

	@Override
	public RequestMonitor getRequestMonitor() {
		final ArcaExecutor executor = (ArcaExecutor) getRequestExecutor();
		return executor.getRequestMonitor();
	}
}
