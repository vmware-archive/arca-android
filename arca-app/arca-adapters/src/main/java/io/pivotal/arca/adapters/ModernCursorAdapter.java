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
package io.pivotal.arca.adapters;

import java.util.Collection;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.View;
import android.widget.ResourceCursorAdapter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernCursorAdapter extends ResourceCursorAdapter {

	private final CursorAdapterHelper mHelper;

	public ModernCursorAdapter(final Context context, final int layout, final Collection<Binding> bindings) {
		super(context, layout, null, 0);
		mHelper = new CursorAdapterHelper(bindings);
	}

	public void setViewBinder(final ViewBinder binder) {
		mHelper.setViewBinder(binder);
	}

	public boolean hasResults() {
		final Cursor cursor = getCursor();
		return cursor != null && cursor.getCount() > 0;
	}

	@Override
	public void bindView(final View container, final Context context, final Cursor cursor) {
		final int type = getItemViewType(cursor.getPosition());
		mHelper.bindView(container, context, cursor, type);
	}
}
