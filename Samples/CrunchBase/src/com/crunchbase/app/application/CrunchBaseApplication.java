package com.crunchbase.app.application;

import android.app.Application;

import com.arca.utils.Logger;

public class CrunchBaseApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Logger.setup(true, "Arca");
	}
}
