package io.pivotal.arca.fragments;

import android.content.ContentResolver;
import android.content.Context;

import io.pivotal.arca.monitor.ArcaExecutor;

public class ArcaExecutorFactory {

	public static ArcaExecutor generateExecutor(final Context context) {
		final ContentResolver resolver = context.getContentResolver();
		return new ArcaExecutor.DefaultArcaExecutor(resolver, context);
	}
}
