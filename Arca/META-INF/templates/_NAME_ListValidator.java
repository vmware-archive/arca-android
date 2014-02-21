package ${base_package}.validators;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.providers.DatasetValidator;
import com.xtreme.rest.loader.ContentState;

public class ${name}ListValidator implements DatasetValidator {

	@Override
	public ContentState validate(final Uri uri, final Cursor cursor) {
		return (cursor.getCount() > 0) ? ContentState.VALID : ContentState.INVALID; 
	}

	@Override
	public boolean fetchData(final Context context, final Uri uri, final Cursor cursor) {
		// return RestService.start(context, new ${name}ListOperation(uri));
		return false;
	}
}
