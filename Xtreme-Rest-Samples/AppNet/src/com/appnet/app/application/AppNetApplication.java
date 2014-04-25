package com.appnet.app.application;

import android.app.Application;

import com.xtreme.rest.providers.Database;

public class AppNetApplication extends Application {
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "appnet.db";

	@Override
	public void onCreate() {
		super.onCreate();
		
		Database.init(DATABASE_NAME, DATABASE_VERSION);
		
		com.xtreme.rest.utils.Logger.setup(true, "Xtreme-Rest");
		com.xtreme.rest.service.Logger.setup(true, "Xtreme-Rest-Service");
		com.xtreme.rest.sync.Logger.setup(true, "Xtreme-Rest-Sync");
	}
}
