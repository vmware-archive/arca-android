package com.arca.fragments;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.Build;

import com.arca.monitor.ArcaDispatcher;
import com.arca.monitor.ArcaExecutor;
import com.arca.monitor.ArcaExecutor.DefaultArcaExecutor;
import com.arca.monitor.ArcaModernDispatcher;
import com.arca.monitor.ArcaSupportDispatcher;

public class ArcaDispatcherFactory {
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static ArcaDispatcher generateDispatcher(android.app.Activity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final ArcaExecutor executor = new DefaultArcaExecutor(resolver, activity);
		return new ArcaModernDispatcher(executor, activity, activity.getLoaderManager());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static ArcaDispatcher generateDispatcher(android.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final ArcaExecutor executor = new DefaultArcaExecutor(resolver, fragment.getActivity());
		return new ArcaModernDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
	
	public static ArcaDispatcher generateDispatcher(android.support.v4.app.FragmentActivity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final ArcaExecutor executor = new DefaultArcaExecutor(resolver, activity);
		return new ArcaSupportDispatcher(executor, activity, activity.getSupportLoaderManager());
	}

	public static ArcaDispatcher generateDispatcher(android.support.v4.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final ArcaExecutor executor = new DefaultArcaExecutor(resolver, fragment.getActivity());
		return new ArcaSupportDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
}
