package ${base_package}.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import ${base_package}.R;
import ${base_package}.fragments.${name}Fragment;

public class ${name}Activity extends FragmentActivity {

	private static final class Extras {
		private static final String ID = "id";
	}
	
	public static final void newInstance(final Context context, final String id) {
		final Intent intent = new Intent(context, ${name}Activity.class);
		intent.putExtra(Extras.ID, id);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_${common.underscore(name)});
		
		final Intent intent = getIntent();
		
		final FragmentManager manager = getSupportFragmentManager();
		final ${name}Fragment fragment = (${name}Fragment) manager.findFragmentById(R.id.fragment_${common.underscore(name)});
		fragment.setId(intent.getStringExtra(Extras.ID));
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		return false;
	}

}
