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
import android.content.ContentResolver;
import android.os.Build;

import io.pivotal.arca.monitor.ArcaDispatcher;
import io.pivotal.arca.monitor.ArcaExecutor;
import io.pivotal.arca.monitor.ArcaModernDispatcher;
import io.pivotal.arca.monitor.ArcaSupportDispatcher;

public class ArcaDispatcherFactory {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static ArcaDispatcher generateDispatcher(android.app.Activity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final ArcaExecutor executor = new ArcaExecutor.DefaultArcaExecutor(resolver, activity);
		return new ArcaModernDispatcher(executor, activity, activity.getLoaderManager());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static ArcaDispatcher generateDispatcher(android.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final ArcaExecutor executor = new ArcaExecutor.DefaultArcaExecutor(resolver, fragment.getActivity());
		return new ArcaModernDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}

	public static ArcaDispatcher generateDispatcher(android.support.v4.app.FragmentActivity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final ArcaExecutor executor = new ArcaExecutor.DefaultArcaExecutor(resolver, activity);
		return new ArcaSupportDispatcher(executor, activity, activity.getSupportLoaderManager());
	}

	public static ArcaDispatcher generateDispatcher(android.support.v4.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final ArcaExecutor executor = new ArcaExecutor.DefaultArcaExecutor(resolver, fragment.getActivity());
		return new ArcaSupportDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
}
