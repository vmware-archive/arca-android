package com.rottentomatoes.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.rottentomatoes.app.R;
import com.rottentomatoes.app.fragments.MovieFragment;

public class MovieActivity extends FragmentActivity {

	private static final class Extras {
		private static final String ID = "id";
	}
	
	public static final void newInstance(final Context context, final String id) {
		final Intent intent = new Intent(context, MovieActivity.class);
		intent.putExtra(Extras.ID, id);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie);
		
		final Intent intent = getIntent();
		
		final FragmentManager manager = getSupportFragmentManager();
		final MovieFragment fragment = (MovieFragment) manager.findFragmentById(R.id.fragment_movie);
		fragment.setId(intent.getStringExtra(Extras.ID));
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		return false;
	}

}
