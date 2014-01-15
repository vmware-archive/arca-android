package com.xtreme.rest.fragments;

import java.util.Collection;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.xtreme.rest.adapters.ItemCursorAdapter;
import com.xtreme.rest.binders.Binding;
import com.xtreme.rest.loader.ContentLoader;
import com.xtreme.rest.loader.ContentResponse;

/**
 * A {@link RestFragment} that adds convenient support a single item by wrapping a {@link ItemCursorAdapter}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class RestItemFragment extends RestFragment {
	
	private ItemCursorAdapter mAdapter;

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAdapterView(view);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			setupAdapterView(getView());
		}
	}
	
	private void setupAdapterView(final View view) {
		mAdapter = onCreateAdapter(view);
	}
	
	/**
	 * A callback to create the {@link Adapter} associated with the {@link AdapterView}.
	 * 
	 * @param adapterView 
	 *
	 * @return
	 */
	public ItemCursorAdapter onCreateAdapter(final View view) {
		return new ItemCursorAdapter(view, getBindings());
	}
	
	protected Collection<Binding> getBindings() {
		return null;
	}
	
	public ItemCursorAdapter getItemAdapter() {
		return mAdapter;
	}
	
	@Override
	public final void onLoaderFinished(final ContentResponse response) {
		getItemAdapter().swapCursor(response.getCursor());
		onContentChanged(response);
	}
	
	@Override
	public final void onLoaderReset() {
		getItemAdapter().swapCursor(null);
		onContentReset();
	}
	
	/**
	 * A callback for when the content is changed, usually from a call to {@link #onLoaderFinished(ContentResponse)}.
	 */
	protected void onContentChanged(final ContentResponse response) {
		
	}
	
	/**
	 * A callback for when the {@link ContentLoader} is reset, usually from a call to {@link #onLoaderReset()}.
	 */
	protected void onContentReset() {
		
	}

}
