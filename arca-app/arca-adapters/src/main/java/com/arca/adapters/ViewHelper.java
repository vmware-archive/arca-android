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
package com.arca.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;

class ViewHelper {

	public static View getView(final View container, final int viewId) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getViewFromHolder(container, viewId);
		} else {
			return getViewFromTag(container, viewId);
		}
	}

	private static View getViewFromTag(final View container, final int viewId) {
		final View view = (View) container.getTag(viewId);
		if (view == null) {
			return getViewAndSetTag(container, viewId);
		} else {
			return view;
		}
	}

    @SuppressLint("ViewTag")
	private static View getViewAndSetTag(final View container, final int viewId) {
		final View view = container.findViewById(viewId);
		container.setTag(viewId, view);
		return view;
	}

	private static View getViewFromHolder(final View container, final int viewId) {
		final ViewHolder holder = (ViewHolder) container.getTag();
		if (holder == null) {
			return getViewAndCreateHolder(container, viewId);
		} else {
			return getViewFromHolder(container, viewId, holder);
		}
	}

	private static View getViewAndCreateHolder(final View container, final int viewId) {
		final ViewHolder holder = new ViewHolder(container);
		return getViewAndAddToHolder(container, viewId, holder);
	}

	private static View getViewAndAddToHolder(final View container, final int viewId, final ViewHolder holder) {
		final View view = container.findViewById(viewId);
		holder.put(viewId, view);
		return view;
	}

	private static View getViewFromHolder(final View container, final int viewId, final ViewHolder holder) {
		final View view = holder.get(viewId);
		if (view == null) {
			return getViewAndAddToHolder(container, viewId, holder);
		} else {
			return view;
		}
	}

	private static final class ViewHolder extends SparseArray<View> {
		public ViewHolder(final View container) {
			container.setTag(this);
		}
	}
}