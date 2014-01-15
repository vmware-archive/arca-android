package com.crunchbase.app.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public abstract class AbsImage  {

	protected static class Fields {
		public static final String AVAILABLE_SIZES = "available_sizes";
		public static final String ATTRIBUTION = "attribution";
	}

    @SerializedName(Fields.AVAILABLE_SIZES) private List<ImageSize> mAvailableSizes;
    @SerializedName(Fields.ATTRIBUTION) private String mAttribution;

    public List<ImageSize> getAvailableSizes() {
        return mAvailableSizes;
    }
    
    public void setAvailableSizes(final List<ImageSize> availableSizes) {
        mAvailableSizes = availableSizes;
    }
    
    public String getAttribution() {
        return mAttribution;
    }
    
    public void setAttribution(final String attribution) {
        mAttribution = attribution;
    }
    
}
