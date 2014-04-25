package ${base_package}.operations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import ${base_package}.models.${name};
import ${base_package}.datasets.${name}Table;
import ${base_package}.providers.${project_name}ContentProvider;
import com.google.gson.Gson;
import com.xtreme.threading.RequestIdentifier;
import com.xtreme.rest.service.Task;

public class ${name}Task extends Task<String> {

	private final String mId;
	
	public ${name}Task(final String id) {
		mId = id;
	}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		// TODO this needs to be a unique identifier across all tasks
		return new RequestIdentifier<String>(String.format("${common.underscore(name)}::%s", mId));
	}
	
	@Override
	public String onExecuteNetworkRequest(final Context context) throws Exception {
		// TODO make network request to fetch object(s)
		// return ${project_name}Requests.get${name}(mId);
		throw new Exception("Override this method to return a json string for a ${name}.");
	}

	@Override
	public void onExecuteProcessingRequest(final Context context, final String data) throws Exception {
		// TODO parse the response and insert into content provider
		final ${name} item = new Gson().fromJson(data, ${name}.class);
		final ContentValues values = ${name}Table.getInstance().getContentValues(item);
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(${project_name}ContentProvider.Uris.${common.pluralUnderscoreUppercase(name)}_URI, values);
	}
}
