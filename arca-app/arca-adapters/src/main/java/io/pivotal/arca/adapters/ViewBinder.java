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

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public interface ViewBinder {
	public boolean setViewValue(View view, Cursor cursor, Binding binding);

	public static class DefaultViewBinder implements ViewBinder {

		@Override
		public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
			if (view instanceof TextView) {
				return setViewValue((TextView) view, cursor, binding);
			}
			return false;
		}

		public boolean setViewValue(final TextView view, final Cursor cursor, final Binding binding) {
			final int columnIndex = binding.getColumnIndex();
			final String text = cursor.getString(columnIndex);
			view.setText(text);
			return true;
		}
	}
}
