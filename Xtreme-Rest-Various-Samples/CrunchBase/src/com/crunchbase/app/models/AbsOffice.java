package com.crunchbase.app.models;

import com.google.gson.annotations.SerializedName;

public abstract class AbsOffice  {

	protected static class Fields {
		public static final String DESCRIPTION = "description";
		public static final String ADDRESS1 = "address1";
		public static final String ADDRESS2 = "address2";
		public static final String ZIP_CODE = "zip_code";
		public static final String CITY = "city";
		public static final String STATE_CODE = "state_code";
		public static final String COUNTRY_CODE = "country_code";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
	}

    @SerializedName(Fields.DESCRIPTION) private String mDescription;
    @SerializedName(Fields.ADDRESS1) private String mAddress1;
    @SerializedName(Fields.ADDRESS2) private String mAddress2;
    @SerializedName(Fields.ZIP_CODE) private String mZipCode;
    @SerializedName(Fields.CITY) private String mCity;
    @SerializedName(Fields.STATE_CODE) private String mStateCode;
    @SerializedName(Fields.COUNTRY_CODE) private String mCountryCode;
    @SerializedName(Fields.LATITUDE) private Float mLatitude;
    @SerializedName(Fields.LONGITUDE) private Float mLongitude;

    public String getDescription() {
        return mDescription;
    }
    
    public void setDescription(final String description) {
        mDescription = description;
    }
    
    public String getAddress1() {
        return mAddress1;
    }
    
    public void setAddress1(final String address1) {
        mAddress1 = address1;
    }
    
    public String getAddress2() {
        return mAddress2;
    }
    
    public void setAddress2(final String address2) {
        mAddress2 = address2;
    }
    
    public String getZipCode() {
        return mZipCode;
    }
    
    public void setZipCode(final String zipCode) {
        mZipCode = zipCode;
    }
    
    public String getCity() {
        return mCity;
    }
    
    public void setCity(final String city) {
        mCity = city;
    }
    
    public String getStateCode() {
        return mStateCode;
    }
    
    public void setStateCode(final String stateCode) {
        mStateCode = stateCode;
    }
    
    public String getCountryCode() {
        return mCountryCode;
    }
    
    public void setCountryCode(final String countryCode) {
        mCountryCode = countryCode;
    }
    
    public Float getLatitude() {
        return mLatitude;
    }
    
    public void setLatitude(final Float latitude) {
        mLatitude = latitude;
    }
    
    public Float getLongitude() {
        return mLongitude;
    }
    
    public void setLongitude(final Float longitude) {
        mLongitude = longitude;
    }
    
}
