package com.crunchbase.app.models;

import com.google.gson.annotations.SerializedName;

public abstract class AbsImageSize  {

	protected static class Fields {
		public static final String WIDTH = "width";
		public static final String HEIGHT = "height";
		public static final String URL = "url";
	}

    @SerializedName(Fields.WIDTH) private Long mWidth;
    @SerializedName(Fields.HEIGHT) private Long mHeight;
    @SerializedName(Fields.URL) private String mUrl;

    public Long getWidth() {
        return mWidth;
    }
    
    public void setWidth(final Long width) {
        mWidth = width;
    }
    
    public Long getHeight() {
        return mHeight;
    }
    
    public void setHeight(final Long height) {
        mHeight = height;
    }
    
    public String getUrl() {
        return mUrl;
    }
    
    public void setUrl(final String url) {
        mUrl = url;
    }
    
}
