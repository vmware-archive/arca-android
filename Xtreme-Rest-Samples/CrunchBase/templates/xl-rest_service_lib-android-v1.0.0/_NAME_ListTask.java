package ${base_package}.operations;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import ${base_package}.models.${name};
import ${base_package}.datasets.${name}Table;
import ${base_package}.providers.${project_name}ContentProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtreme.threading.RequestIdentifier;
import com.xtreme.rest.service.Task;

public class ${name}ListTask extends Task<String> {

	public ${name}ListTask() {
		
	}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		// This needs to be a unique identifier across all tasks
		return new RequestIdentifier<String>("${common.underscore(name)}_list");
	}
	
	@Override
	public String onExecuteNetworkRequest(final Context context) throws Exception {
		// TODO make network request to fetch object(s) 
		// return ${project_name}Requests.get${name}List();
		throw new Exception("Override this method to return a json string of ${name} items.");
	}

	@Override
	public void onExecuteProcessingRequest(final Context context, final String data) throws Exception {
		// Parse the response and insert into the content provider
		final Type listType = new TypeToken<ArrayList<${name}>>(){}.getType();
		final List<${name}> list = new Gson().fromJson(data, listType);
		final ContentValues[] values = ${name}Table.getInstance().getContentValues(list);
		final ContentResolver resolver = context.getContentResolver();
		resolver.bulkInsert(${project_name}ContentProvider.Uris.${common.pluralUnderscoreUppercase(name)}_URI, values);
	}
}
