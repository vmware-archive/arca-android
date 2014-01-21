package com.xtreme.rest.dispatcher;

import com.xtreme.rest.dispatcher.RequestExecutor.DefaultRequestExecutor;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.Build;

public class RequestDispatcherFactory {
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static RequestDispatcher generateContentLoader(android.app.Activity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final DefaultRequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new ModernRequestDispatcher(executor, activity, activity.getLoaderManager());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static RequestDispatcher generateContentLoader(android.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final DefaultRequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new ModernRequestDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
	
	public static RequestDispatcher generateContentLoader(android.support.v4.app.FragmentActivity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final DefaultRequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new SupportRequestDispatcher(executor, activity, activity.getSupportLoaderManager());
	}

	public static RequestDispatcher generateContentLoader(android.support.v4.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final DefaultRequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new SupportRequestDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
}
