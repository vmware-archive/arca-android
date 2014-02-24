package com.appnet.app.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public abstract class AbsPostsResponse  {

	protected static class Fields {
		public static final String DATA = "data";
	}

    @SerializedName(Fields.DATA) private List<Post> mData;

    public List<Post> getData() {
        return mData;
    }
    
    public void setData(final List<Post> data) {
        mData = data;
    }
    
}
