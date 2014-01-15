package com.xtreme.rest.loader;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Defines a single request for content. To be used with a {@link ContentLoader}. It contains the {@link Uri},
 * projections, where clause & args, sort order, and the force update flag.
 */
public class ContentRequest implements Parcelable {
	
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
	
	public ContentRequest(final Parcel in) {
		mContentUri = in.readParcelable(Uri.class.getClassLoader());
		mProjection = in.createStringArray();
		mWhereClause = in.readString();
		mWhereArgs = in.createStringArray();
		mSortOrder = in.readString();
		mForceUpdate = in.readInt() == 1;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(mContentUri, flags);
		dest.writeStringArray(mProjection);
		dest.writeString(mWhereClause);
		dest.writeStringArray(mWhereArgs);
		dest.writeString(mSortOrder);
		dest.writeInt(mForceUpdate ? 1 : 0);
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
	
	public static final Parcelable.Creator<ContentRequest> CREATOR = new Parcelable.Creator<ContentRequest>() {
		@Override
		public ContentRequest createFromParcel(final Parcel in) {
			return new ContentRequest(in);
		}

		@Override
		public ContentRequest[] newArray(final int size) {
			return new ContentRequest[size];
		}
	};
	
}
