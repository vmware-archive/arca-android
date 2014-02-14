package com.xtreme.rest.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

public interface ViewAnimator {
	public void animateView(final View view, final Context context, final Cursor cursor);
}
