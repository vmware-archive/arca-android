package com.appnet.app.application;

import android.app.Application;

import com.arca.utils.Logger;

public class AppNetApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Logger.setup(true, "Arca");
	}
}
