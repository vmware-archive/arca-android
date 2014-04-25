package com.crunchbase.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.crunchbase.app.R;
import com.crunchbase.app.fragments.CompanyFragment;

public class CompanyActivity extends FragmentActivity {

	private static final class Extras {
		private static final String ID = "id";
	}
	
	public static final void newInstance(final Context context, final String id) {
		final Intent intent = new Intent(context, CompanyActivity.class);
		intent.putExtra(Extras.ID, id);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company);
		
		final Intent intent = getIntent();
		
		final FragmentManager manager = getSupportFragmentManager();
		final CompanyFragment fragment = (CompanyFragment) manager.findFragmentById(R.id.fragment_company);
		fragment.setId(intent.getStringExtra(Extras.ID));
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		return false;
	}

}
