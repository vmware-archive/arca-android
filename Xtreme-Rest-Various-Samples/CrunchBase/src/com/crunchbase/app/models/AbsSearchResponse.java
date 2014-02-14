package com.crunchbase.app.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public abstract class AbsSearchResponse  {

	protected static class Fields {
		public static final String TOTAL = "total";
		public static final String PAGE = "page";
		public static final String RESULTS = "results";
	}

    @SerializedName(Fields.TOTAL) private Integer mTotal;
    @SerializedName(Fields.PAGE) private Integer mPage;
    @SerializedName(Fields.RESULTS) private List<Company> mResults;

    public Integer getTotal() {
        return mTotal;
    }
    
    public void setTotal(final Integer total) {
        mTotal = total;
    }
    
    public Integer getPage() {
        return mPage;
    }
    
    public void setPage(final Integer page) {
        mPage = page;
    }
    
    public List<Company> getResults() {
        return mResults;
    }
    
    public void setResults(final List<Company> results) {
        mResults = results;
    }
    
}
