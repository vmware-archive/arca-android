package com.xtreme.rest.service.test.android;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new Tests(this).run();
	}
	
}
