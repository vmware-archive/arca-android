package com.rottentomatoes.app.application;

import android.app.Application;

import com.xtreme.rest.providers.Database;

public class RottenTomatoesApplication extends Application {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "rottentomatoes.db";

	@Override
	public void onCreate() {
		super.onCreate();
		
		Database.init(DATABASE_NAME, DATABASE_VERSION);
		
		com.xtreme.rest.utils.Logger.setup(true, "Xtreme-Rest");
		com.xtreme.rest.service.Logger.setup(true, "Xtreme-Rest-Service");
		com.xtreme.rest.sync.Logger.setup(true, "Xtreme-Rest-Sync");
	}
}
