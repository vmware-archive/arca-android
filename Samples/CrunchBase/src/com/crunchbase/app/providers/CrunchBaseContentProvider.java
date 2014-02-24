package com.crunchbase.app.providers;

import android.net.Uri;

import com.arca.ArcaContentProvider;
import com.crunchbase.app.datasets.CompanyTable;

public class CrunchBaseContentProvider extends ArcaContentProvider {

	public static final String AUTHORITY = "com.crunchbase.app.providers.CrunchBaseContentProvider";
	
	public static class Uris {
		public static final Uri COMPANIES_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.COMPANIES);
	}
	
	protected static class Paths {
		public static final String COMPANIES = "companies";
	}
	
	@Override
	public boolean onCreate() {
		super.onCreate();
        registerDataset(AUTHORITY, Paths.COMPANIES, CompanyTable.class);
		registerDataset(AUTHORITY, Paths.COMPANIES + "/*", CompanyTable.class);
		return true;
	}
}