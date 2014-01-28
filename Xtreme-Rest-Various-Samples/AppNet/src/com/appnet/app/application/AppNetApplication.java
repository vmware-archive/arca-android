package com.appnet.app.application;

import android.app.Application;

public class AppNetApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		com.xtreme.rest.utils.Logger.setup(true, "Xtreme-Rest");
	}
}
