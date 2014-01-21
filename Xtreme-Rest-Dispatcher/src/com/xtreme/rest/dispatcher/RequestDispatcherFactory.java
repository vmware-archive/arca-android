package com.xtreme.rest.dispatcher;

import com.xtreme.rest.dispatcher.RequestExecutor.DefaultRequestExecutor;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.Build;

public class RequestDispatcherFactory {
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static RequestDispatcher generateRequestDispatcher(android.app.Activity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final RequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new ModernRequestDispatcher(executor, activity, activity.getLoaderManager());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static RequestDispatcher generateRequestDispatcher(android.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final RequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new ModernRequestDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
	
	public static RequestDispatcher generateRequestDispatcher(android.support.v4.app.FragmentActivity activity) {
		final ContentResolver resolver = activity.getContentResolver();
		final RequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new SupportRequestDispatcher(executor, activity, activity.getSupportLoaderManager());
	}

	public static RequestDispatcher generateRequestDispatcher(android.support.v4.app.Fragment fragment) {
		final ContentResolver resolver = fragment.getActivity().getContentResolver();
		final RequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new SupportRequestDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
}
