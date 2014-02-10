package com.rottentomatoes.app.application;

import android.app.Application;
import android.support.v4.app.LoaderManager;

import com.xtreme.rest.utils.Logger;

public class RottenTomatoesApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Logger.setup(true, "Xtreme-Rest");
		
		LoaderManager.enableDebugLogging(true);
	}
}
