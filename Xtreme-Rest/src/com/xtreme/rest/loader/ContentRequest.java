package com.xtreme.rest.loader;

import com.xtreme.rest.providers.RestContentProvider;

import android.content.CursorLoader;
import android.net.Uri;

/**
 * Defines a single request for content. To be used with a {@link ContentLoader}. It contains the {@link Uri},
 * projections, where clause & args, sort order, and the force update flag. All arguments are used up by the
 * {@link RestContentProvider} and sent through a {@link CursorLoader}.
 */
public class ContentRequest {
	
	public static final class Params {
		public static final String FORCE_UPDATE = "force_update";
	}
	
	private final Uri mContentUri;
	private String[] mProjection;
	private String mWhereClause;
	private String[] mWhereArgs;
	private String mSortOrder;
	private boolean mForceUpdate;

	public ContentRequest(final Uri uri) {
		if (uri == null) {
			throw new IllegalArgumentException("The URI must not be null.");
		}
		mContentUri = uri;
	}

	public void setProjection(final String[] projection) {
		mProjection = projection;
	}

	public void setWhere(final String whereClause, final String[] whereArgs) {
		mWhereClause = whereClause;
		mWhereArgs = whereArgs;
	}

	public void setSortOrder(final String sortOrder) {
		mSortOrder = sortOrder;
	}

	public void setForceUpdate(final boolean forceUpdate) {
		mForceUpdate = forceUpdate;
	}
	
	public Uri getContentUri() {
		return mContentUri;
	}

	public String[] getProjection() {
		return mProjection;
	}

	public String getWhereClause() {
		return mWhereClause;
	}

	public String[] getWhereArgs() {
		return mWhereArgs;
	}

	public String getSortOrder() {
		return mSortOrder;
	}

	public boolean shouldForceUpdate() {
		return mForceUpdate;
	}
	
	/**
	 * @return The {@link Uri} with the force update query parameter appended.
	 */
	public Uri getForceUpdateContentUri() {
		if (mForceUpdate) { 
			mForceUpdate = false;
			final Uri.Builder builder = mContentUri.buildUpon();
			builder.appendQueryParameter(Params.FORCE_UPDATE, "true");
			return builder.build();
		} else {
			return getContentUri();
		}
	}
}
