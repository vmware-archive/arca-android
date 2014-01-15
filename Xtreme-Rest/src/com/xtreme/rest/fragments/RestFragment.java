package com.xtreme.rest.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.xtreme.rest.loader.ContentLoader;
import com.xtreme.rest.loader.ContentLoaderFactory;
import com.xtreme.rest.loader.ContentLoaderListener;
import com.xtreme.rest.loader.ContentRequest;

/**
 * This class provides a basic implementation of a single {@link ContentLoader} for a fragment. Using this class, fragments can request data
 * simply by calling {@link RestFragment#execute(ContentRequest)}.<br>
 * <br>
 * <b>IMPORTANT:</b> You MUST call the super when onViewCreated and onDestroyView is called in your fragment!<br>
 * <br>
 * <b>ALSO IMPORTANT:</b> {@link RestFragment#execute(ContentRequest)} may only be called after onViewCreated has been called and
 * before onDestroyView has been called.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class RestFragment extends Fragment implements ContentLoaderListener {
	
	private ContentLoader mContentLoader;
	
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mContentLoader = ContentLoaderFactory.generateContentLoader(this, this);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			mContentLoader = ContentLoaderFactory.generateContentLoader(this, this);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) { 
			getContentLoader().destroy();
		}
	}
	
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void onDestroyView() {
		getContentLoader().destroy();
		
		super.onDestroyView();
	}
	
	/**
	 * @return The {@link ContentLoader} instance used by this fragment.
	 */
	protected final ContentLoader getContentLoader() {
		return mContentLoader;
	}
	
	/**
	 * @see ContentLoader#execute(ContentRequest)
	 */
	protected final void execute(final ContentRequest request) {
		getContentLoader().execute(request);
	}
}
