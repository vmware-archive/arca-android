package com.appnet.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.appnet.app.R;

public class PostListActivity extends FragmentActivity {

	public static final void newInstance(final Context context) {
		final Intent intent = new Intent(context, PostListActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_list);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		return false;
	}

}
