package com.rottentomatoes.app.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public abstract class AbsMovie  {

	protected static class Fields {
		public static final String ID = "id";
		public static final String TITLE = "title";
		public static final String YEAR = "year";
		public static final String MPAA_RATING = "mpaa_rating";
		public static final String RUNTIME = "runtime";
		public static final String CRITICS_CONSENSUS = "critics_consensus";
		public static final String RELEASE_DATES = "release_dates";
		public static final String RATINGS = "ratings";
		public static final String SYNOPSIS = "synopsis";
		public static final String POSTERS = "posters";
		public static final String ABRIDGED_CAST = "abridged_cast";
	}

    @SerializedName(Fields.ID) private String mId;
    @SerializedName(Fields.TITLE) private String mTitle;
    @SerializedName(Fields.YEAR) private String mYear;
    @SerializedName(Fields.MPAA_RATING) private String mMpaaRating;
    @SerializedName(Fields.RUNTIME) private String mRuntime;
    @SerializedName(Fields.CRITICS_CONSENSUS) private String mCriticsConsensus;
    @SerializedName(Fields.RELEASE_DATES) private ReleaseDates mReleaseDates;
    @SerializedName(Fields.RATINGS) private Ratings mRatings;
    @SerializedName(Fields.SYNOPSIS) private String mSynopsis;
    @SerializedName(Fields.POSTERS) private Posters mPosters;
    @SerializedName(Fields.ABRIDGED_CAST) private List<Cast> mAbridgedCast;

    public String getId() {
        return mId;
    }
    
    public void setId(final String id) {
        mId = id;
    }
    
    public String getTitle() {
        return mTitle;
    }
    
    public void setTitle(final String title) {
        mTitle = title;
    }
    
    public String getYear() {
        return mYear;
    }
    
    public void setYear(final String year) {
        mYear = year;
    }
    
    public String getMpaaRating() {
        return mMpaaRating;
    }
    
    public void setMpaaRating(final String mpaaRating) {
        mMpaaRating = mpaaRating;
    }
    
    public String getRuntime() {
        return mRuntime;
    }
    
    public void setRuntime(final String runtime) {
        mRuntime = runtime;
    }
    
    public String getCriticsConsensus() {
        return mCriticsConsensus;
    }
    
    public void setCriticsConsensus(final String criticsConsensus) {
        mCriticsConsensus = criticsConsensus;
    }
    
    public ReleaseDates getReleaseDates() {
        return mReleaseDates;
    }
    
    public void setReleaseDates(final ReleaseDates releaseDates) {
        mReleaseDates = releaseDates;
    }
    
    public Ratings getRatings() {
        return mRatings;
    }
    
    public void setRatings(final Ratings ratings) {
        mRatings = ratings;
    }
    
    public String getSynopsis() {
        return mSynopsis;
    }
    
    public void setSynopsis(final String synopsis) {
        mSynopsis = synopsis;
    }
    
    public Posters getPosters() {
        return mPosters;
    }
    
    public void setPosters(final Posters posters) {
        mPosters = posters;
    }
    
    public List<Cast> getAbridgedCast() {
        return mAbridgedCast;
    }
    
    public void setAbridgedCast(final List<Cast> abridgedCast) {
        mAbridgedCast = abridgedCast;
    }
    
}
