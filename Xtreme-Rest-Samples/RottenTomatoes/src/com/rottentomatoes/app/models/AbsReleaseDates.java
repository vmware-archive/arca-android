package com.rottentomatoes.app.models;

import com.google.gson.annotations.SerializedName;

public abstract class AbsReleaseDates  {

	protected static class Fields {
		public static final String THEATER = "theater";
	}

    @SerializedName(Fields.THEATER) private String mTheater;

    public String getTheater() {
        return mTheater;
    }
    
    public void setTheater(final String theater) {
        mTheater = theater;
    }
    
}
