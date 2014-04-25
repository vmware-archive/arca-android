package ${base_package}.application;

import android.app.Application;

import com.xtreme.rest.providers.Database;

public class ${project_name}Application extends Application {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "${common.underscore(project_name)}.db";

	@Override
	public void onCreate() {
		super.onCreate();
		
		Database.init(DATABASE_NAME, DATABASE_VERSION);
	}
}
