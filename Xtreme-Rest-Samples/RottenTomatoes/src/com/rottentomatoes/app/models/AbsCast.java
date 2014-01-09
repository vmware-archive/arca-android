package com.rottentomatoes.app.models;

import com.google.gson.annotations.SerializedName;

public abstract class AbsCast  {

	protected static class Fields {
		public static final String ID = "id";
		public static final String NAME = "name";
	}

    @SerializedName(Fields.ID) private String mId;
    @SerializedName(Fields.NAME) private String mName;

    public String getId() {
        return mId;
    }
    
    public void setId(final String id) {
        mId = id;
    }
    
    public String getName() {
        return mName;
    }
    
    public void setName(final String name) {
        mName = name;
    }
    
}
