package com.appnet.app.models;

import com.google.gson.annotations.SerializedName;

public abstract class AbsUser  {

	protected static class Fields {
		public static final String ID = "id";
		public static final String USERNAME = "username";
		public static final String NAME = "name";
		public static final String TYPE = "type";
		public static final String AVATAR_IMAGE = "avatar_image";
	}

    @SerializedName(Fields.ID) private String mId;
    @SerializedName(Fields.USERNAME) private String mUsername;
    @SerializedName(Fields.NAME) private String mName;
    @SerializedName(Fields.TYPE) private String mType;
    @SerializedName(Fields.AVATAR_IMAGE) private AvatarImage mAvatarImage;

    public String getId() {
        return mId;
    }
    
    public void setId(final String id) {
        mId = id;
    }
    
    public String getUsername() {
        return mUsername;
    }
    
    public void setUsername(final String username) {
        mUsername = username;
    }
    
    public String getName() {
        return mName;
    }
    
    public void setName(final String name) {
        mName = name;
    }
    
    public String getType() {
        return mType;
    }
    
    public void setType(final String type) {
        mType = type;
    }
    
    public AvatarImage getAvatarImage() {
        return mAvatarImage;
    }
    
    public void setAvatarImage(final AvatarImage avatarImage) {
        mAvatarImage = avatarImage;
    }
    
}
