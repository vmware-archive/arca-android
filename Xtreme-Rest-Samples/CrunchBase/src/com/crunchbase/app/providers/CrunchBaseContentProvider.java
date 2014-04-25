package com.crunchbase.app.providers;

import android.net.Uri;

import com.crunchbase.app.datasets.CompanyTable;

import com.crunchbase.app.validators.CompanyListValidator;
import com.crunchbase.app.validators.CompanyValidator;

import com.xtreme.rest.providers.RestContentProvider;

public class CrunchBaseContentProvider extends RestContentProvider {

	public static final String AUTHORITY = "com.crunchbase.app.providers.CrunchBaseContentProvider";
	
	public static class Uris {
		public static final Uri COMPANIES_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.COMPANIES);
	}
	
	protected static class Paths {
		public static final String COMPANIES = "companies";
	}
	
	@Override
	public boolean onCreate() {
        registerDataset(AUTHORITY, Paths.COMPANIES, CompanyTable.class, CompanyListValidator.class);
		registerDataset(AUTHORITY, Paths.COMPANIES + "/*", CompanyTable.class, CompanyValidator.class);
		return true;
	}
}