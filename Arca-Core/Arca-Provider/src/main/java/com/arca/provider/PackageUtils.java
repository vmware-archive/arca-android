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
package com.arca.provider;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtils {

	public static int getVersionCode(final Context context) {
		final String packageName = getPackageName(context);
		return getVersionCode(context, packageName);
	}

	public static int getVersionCode(final Context context, final String packageName) {
		try {
			return context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
		} catch (final NameNotFoundException e) {
			return -1;
		}
	}

	public static String getPackageName(final Context context) {
		return context.getPackageName();
	}

}
