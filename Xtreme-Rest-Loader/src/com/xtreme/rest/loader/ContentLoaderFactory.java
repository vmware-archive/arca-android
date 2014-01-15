package com.xtreme.rest.loader;

import android.annotation.TargetApi;
import android.os.Build;

public class ContentLoaderFactory {
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static ContentLoader generateContentLoader(android.app.Activity activity, ContentLoaderListener listener) {
		return new ModernContentLoader(activity, listener, activity.getLoaderManager());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static ContentLoader generateContentLoader(android.app.Fragment fragment, ContentLoaderListener listener) {
		return new ModernContentLoader(fragment.getActivity(), listener, fragment.getLoaderManager());
	}
	
	public static ContentLoader generateContentLoader(android.support.v4.app.FragmentActivity activity, ContentLoaderListener listener) {
		return new SupportContentLoader(activity, listener, activity.getSupportLoaderManager());
	}

	public static ContentLoader generateContentLoader(android.support.v4.app.Fragment fragment, ContentLoaderListener listener) {
		return new SupportContentLoader(fragment.getActivity(), listener, fragment.getLoaderManager());
	}
}
