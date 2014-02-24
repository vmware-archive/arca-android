package com.rottentomatoes.app.application;

import android.app.Application;

import com.arca.utils.Logger;

public class RottenTomatoesApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Logger.setup(true, "Arca");
	}
}
