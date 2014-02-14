package com.crunchbase.app.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public abstract class AbsCompany  {

	protected static class Fields {
		public static final String NAME = "name";
		public static final String CATEGORY_CODE = "category_code";
		public static final String DESCRIPTION = "description";
		public static final String PERMALINK = "permalink";
		public static final String CRUNCHBASE_URL = "crunchbase_url";
		public static final String HOMEPAGE_URL = "homepage_url";
		public static final String NAMESPACE = "namespace";
		public static final String OVERVIEW = "overview";
		public static final String IMAGE = "image";
		public static final String OFFICES = "offices";
	}

    @SerializedName(Fields.NAME) private String mName;
    @SerializedName(Fields.CATEGORY_CODE) private String mCategoryCode;
    @SerializedName(Fields.DESCRIPTION) private String mDescription;
    @SerializedName(Fields.PERMALINK) private String mPermalink;
    @SerializedName(Fields.CRUNCHBASE_URL) private String mCrunchbaseUrl;
    @SerializedName(Fields.HOMEPAGE_URL) private String mHomepageUrl;
    @SerializedName(Fields.NAMESPACE) private String mNamespace;
    @SerializedName(Fields.OVERVIEW) private String mOverview;
    @SerializedName(Fields.IMAGE) private Image mImage;
    @SerializedName(Fields.OFFICES) private List<Office> mOffices;

    public String getName() {
        return mName;
    }
    
    public void setName(final String name) {
        mName = name;
    }
    
    public String getCategoryCode() {
        return mCategoryCode;
    }
    
    public void setCategoryCode(final String categoryCode) {
        mCategoryCode = categoryCode;
    }
    
    public String getDescription() {
        return mDescription;
    }
    
    public void setDescription(final String description) {
        mDescription = description;
    }
    
    public String getPermalink() {
        return mPermalink;
    }
    
    public void setPermalink(final String permalink) {
        mPermalink = permalink;
    }
    
    public String getCrunchbaseUrl() {
        return mCrunchbaseUrl;
    }
    
    public void setCrunchbaseUrl(final String crunchbaseUrl) {
        mCrunchbaseUrl = crunchbaseUrl;
    }
    
    public String getHomepageUrl() {
        return mHomepageUrl;
    }
    
    public void setHomepageUrl(final String homepageUrl) {
        mHomepageUrl = homepageUrl;
    }
    
    public String getNamespace() {
        return mNamespace;
    }
    
    public void setNamespace(final String namespace) {
        mNamespace = namespace;
    }
    
    public String getOverview() {
        return mOverview;
    }
    
    public void setOverview(final String overview) {
        mOverview = overview;
    }
    
    public Image getImage() {
        return mImage;
    }
    
    public void setImage(final Image image) {
        mImage = image;
    }
    
    public List<Office> getOffices() {
        return mOffices;
    }
    
    public void setOffices(final List<Office> offices) {
        mOffices = offices;
    }
    
}
