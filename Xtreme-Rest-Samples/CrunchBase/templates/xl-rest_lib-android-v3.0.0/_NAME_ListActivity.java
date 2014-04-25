package ${base_package}.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import ${base_package}.R;

public class ${name}ListActivity extends FragmentActivity {

	public static final void newInstance(final Context context) {
		final Intent intent = new Intent(context, ${name}ListActivity.class);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_${name.lower()}_list);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		return false;
	}

}
