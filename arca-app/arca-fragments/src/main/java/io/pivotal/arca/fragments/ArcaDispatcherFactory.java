package io.pivotal.arca.fragments;

import android.annotation.TargetApi;
import android.os.Build;

import io.pivotal.arca.monitor.ArcaDispatcher;
import io.pivotal.arca.monitor.ArcaExecutor;
import io.pivotal.arca.monitor.ArcaModernDispatcher;
import io.pivotal.arca.monitor.ArcaSupportDispatcher;

public class ArcaDispatcherFactory {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static ArcaDispatcher generateDispatcher(android.app.Activity activity) {
		final ArcaExecutor executor = ArcaExecutorFactory.generateExecutor(activity);
		return new ArcaModernDispatcher(executor, activity, activity.getLoaderManager());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static ArcaDispatcher generateDispatcher(android.app.Fragment fragment) {
		final ArcaExecutor executor = ArcaExecutorFactory.generateExecutor(fragment.getActivity());
		return new ArcaModernDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}

	public static ArcaDispatcher generateDispatcher(android.support.v4.app.FragmentActivity activity) {
		final ArcaExecutor executor = ArcaExecutorFactory.generateExecutor(activity);
		return new ArcaSupportDispatcher(executor, activity, activity.getSupportLoaderManager());
	}

	public static ArcaDispatcher generateDispatcher(android.support.v4.app.Fragment fragment) {
		final ArcaExecutor executor = ArcaExecutorFactory.generateExecutor(fragment.getActivity());
		return new ArcaSupportDispatcher(executor, fragment.getActivity(), fragment.getLoaderManager());
	}
}
