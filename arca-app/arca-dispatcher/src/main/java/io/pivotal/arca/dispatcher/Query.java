package io.pivotal.arca.dispatcher;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import io.pivotal.arca.utils.ArrayUtils;
import io.pivotal.arca.utils.StringUtils;

public class Query extends Request<Cursor> implements Parcelable {

	private String[] mProjection;
	private String mWhereClause;
	private String[] mWhereArgs;
	private String mSortOrder;
	private boolean mForceUpdate;

	public Query(final Uri uri) {
		super(uri);
	}

	public Query(final Uri uri, final int identifier) {
		super(uri, identifier);
	}

	public Query(final Parcel in) {
		super(in);
		mProjection = in.createStringArray();
		mWhereClause = in.readString();
		mWhereArgs = in.createStringArray();
		mSortOrder = in.readString();
		mForceUpdate = in.readInt() == 1;
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeStringArray(mProjection);
		dest.writeString(mWhereClause);
		dest.writeStringArray(mWhereArgs);
		dest.writeString(mSortOrder);
		dest.writeInt(mForceUpdate ? 1 : 0);
	}

	public void setProjection(final String[] projection) {
		mProjection = projection;
	}

	public void setWhere(final String whereClause, final String... whereArgs) {
		mWhereClause = whereClause;
		mWhereArgs = whereArgs;
	}

	public void addWhere(final String whereClause, final String... whereArgs) {
        mWhereClause = StringUtils.append(mWhereClause, whereClause, " AND ");
        mWhereArgs = ArrayUtils.append(mWhereArgs, whereArgs);
	}

	public void setSortOrder(final String sortOrder) {
		mSortOrder = sortOrder;
	}

	public void setForceUpdate(final boolean forceUpdate) {
		mForceUpdate = forceUpdate;
	}

	public boolean shouldForceUpdate() {
		return mForceUpdate;
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

	public static final Creator<Query> CREATOR = new Creator<Query>() {
		@Override
		public Query createFromParcel(final Parcel in) {
			return new Query(in);
		}

		@Override
		public Query[] newArray(final int size) {
			return new Query[size];
		}
	};
}
