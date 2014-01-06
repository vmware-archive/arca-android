package com.appnet.app.models;

import com.google.gson.annotations.SerializedName;

public abstract class AbsPost  {

	protected static class Fields {
		public static final String ID = "id";
		public static final String CREATED_AT = "created_at";
		public static final String TEXT = "text";
		public static final String NUM_STARS = "num_stars";
		public static final String NUM_REPOSTS = "num_reposts";
		public static final String NUM_REPLIES = "num_replies";
		public static final String USER = "user";
	}

    @SerializedName(Fields.ID) private String mId;
    @SerializedName(Fields.CREATED_AT) private String mCreatedAt;
    @SerializedName(Fields.TEXT) private String mText;
    @SerializedName(Fields.NUM_STARS) private String mNumStars;
    @SerializedName(Fields.NUM_REPOSTS) private String mNumReposts;
    @SerializedName(Fields.NUM_REPLIES) private String mNumReplies;
    @SerializedName(Fields.USER) private User mUser;

    public String getId() {
        return mId;
    }
    
    public void setId(final String id) {
        mId = id;
    }
    
    public String getCreatedAt() {
        return mCreatedAt;
    }
    
    public void setCreatedAt(final String createdAt) {
        mCreatedAt = createdAt;
    }
    
    public String getText() {
        return mText;
    }
    
    public void setText(final String text) {
        mText = text;
    }
    
    public String getNumStars() {
        return mNumStars;
    }
    
    public void setNumStars(final String numStars) {
        mNumStars = numStars;
    }
    
    public String getNumReposts() {
        return mNumReposts;
    }
    
    public void setNumReposts(final String numReposts) {
        mNumReposts = numReposts;
    }
    
    public String getNumReplies() {
        return mNumReplies;
    }
    
    public void setNumReplies(final String numReplies) {
        mNumReplies = numReplies;
    }
    
    public User getUser() {
        return mUser;
    }
    
    public void setUser(final User user) {
        mUser = user;
    }
    
}
