package com.xtreme.rest.service.test.android;

import com.xtreme.rest.service.Logger;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Logger.setup(true, "RESTEST");
		
		new Tests(this).run();
	}
	
}
