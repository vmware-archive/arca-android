package com.xtreme.rest.fragments;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.Build;

import com.xtreme.rest.monitor.RestDispatcher;
import com.xtreme.rest.monitor.RestExecutor;
import com.xtreme.rest.monitor.RestExecutor.DefaultRestExecutor;
import com.xtreme.rest.monitor.RestModernDispatcher;
import com.xtreme.rest.monitor.RestSupportDispatcher;

public class RestDispatcherFactory {
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static RestDispatcher generateDispatcher(android.app.Activity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final RestExecutor executor = new DefaultRestExecutor(resolver, activity);
		return new RestModernDispatcher(executor, activity, activity.getLoaderManager());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static RestDispatcher generateDispatcher(android.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final RestExecutor executor = new DefaultRestExecutor(resolver, fragment.getActivity());
		return new RestModernDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
	
	public static RestDispatcher generateDispatcher(android.support.v4.app.FragmentActivity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final RestExecutor executor = new DefaultRestExecutor(resolver, activity);
		return new RestSupportDispatcher(executor, activity, activity.getSupportLoaderManager());
	}

	public static RestDispatcher generateDispatcher(android.support.v4.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final RestExecutor executor = new DefaultRestExecutor(resolver, fragment.getActivity());
		return new RestSupportDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
}
