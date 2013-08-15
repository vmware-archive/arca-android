package com.xtreme.rest.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtils {

	public static int getVersionCode(final Context context) {
		try {
			final String packageName = getPackageName(context);
			return context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
		} catch (final NameNotFoundException e) {
			return -1;
		}
	}

	public static String getPackageName(final Context context) {
		return context.getPackageName();
	}

}
