package com.appnet.app.models;

import com.google.gson.annotations.SerializedName;

public abstract class AbsAvatarImage  {

	protected static class Fields {
		public static final String URL = "url";
		public static final String WIDTH = "width";
		public static final String HEIGHT = "height";
	}

    @SerializedName(Fields.URL) private String mUrl;
    @SerializedName(Fields.WIDTH) private String mWidth;
    @SerializedName(Fields.HEIGHT) private String mHeight;

    public String getUrl() {
        return mUrl;
    }
    
    public void setUrl(final String url) {
        mUrl = url;
    }
    
    public String getWidth() {
        return mWidth;
    }
    
    public void setWidth(final String width) {
        mWidth = width;
    }
    
    public String getHeight() {
        return mHeight;
    }
    
    public void setHeight(final String height) {
        mHeight = height;
    }
    
}
