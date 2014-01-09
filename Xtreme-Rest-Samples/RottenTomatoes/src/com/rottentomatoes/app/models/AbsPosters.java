package com.rottentomatoes.app.models;

import com.google.gson.annotations.SerializedName;

public abstract class AbsPosters  {

	protected static class Fields {
		public static final String THUMBNAIL = "thumbnail";
		public static final String PROFILE = "profile";
		public static final String DETAILED = "detailed";
		public static final String ORIGINAL = "original";
	}

    @SerializedName(Fields.THUMBNAIL) private String mThumbnail;
    @SerializedName(Fields.PROFILE) private String mProfile;
    @SerializedName(Fields.DETAILED) private String mDetailed;
    @SerializedName(Fields.ORIGINAL) private String mOriginal;

    public String getThumbnail() {
        return mThumbnail;
    }
    
    public void setThumbnail(final String thumbnail) {
        mThumbnail = thumbnail;
    }
    
    public String getProfile() {
        return mProfile;
    }
    
    public void setProfile(final String profile) {
        mProfile = profile;
    }
    
    public String getDetailed() {
        return mDetailed;
    }
    
    public void setDetailed(final String detailed) {
        mDetailed = detailed;
    }
    
    public String getOriginal() {
        return mOriginal;
    }
    
    public void setOriginal(final String original) {
        mOriginal = original;
    }
    
}
