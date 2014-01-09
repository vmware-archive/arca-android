package com.rottentomatoes.app.models;

import com.google.gson.annotations.SerializedName;

public abstract class AbsRatings  {

	protected static class Fields {
		public static final String CRITICS_RATING = "critics_rating";
		public static final String CRITICS_SCORE = "critics_score";
		public static final String AUDIENCE_RATING = "audience_rating";
		public static final String AUDIENCE_SCORE = "audience_score";
	}

    @SerializedName(Fields.CRITICS_RATING) private String mCriticsRating;
    @SerializedName(Fields.CRITICS_SCORE) private Integer mCriticsScore;
    @SerializedName(Fields.AUDIENCE_RATING) private String mAudienceRating;
    @SerializedName(Fields.AUDIENCE_SCORE) private Integer mAudienceScore;

    public String getCriticsRating() {
        return mCriticsRating;
    }
    
    public void setCriticsRating(final String criticsRating) {
        mCriticsRating = criticsRating;
    }
    
    public Integer getCriticsScore() {
        return mCriticsScore;
    }
    
    public void setCriticsScore(final Integer criticsScore) {
        mCriticsScore = criticsScore;
    }
    
    public String getAudienceRating() {
        return mAudienceRating;
    }
    
    public void setAudienceRating(final String audienceRating) {
        mAudienceRating = audienceRating;
    }
    
    public Integer getAudienceScore() {
        return mAudienceScore;
    }
    
    public void setAudienceScore(final Integer audienceScore) {
        mAudienceScore = audienceScore;
    }
    
}
