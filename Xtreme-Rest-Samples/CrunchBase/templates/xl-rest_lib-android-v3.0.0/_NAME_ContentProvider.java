package ${base_package}.providers;

import android.net.Uri;

% for name, model in model_lookup.items():
import ${base_package}.datasets.${name}Table;
%endfor

% for name, model in model_lookup.items():
import ${base_package}.validators.${name}ListValidator;
import ${base_package}.validators.${name}Validator;
%endfor

import com.xtreme.rest.providers.RestContentProvider;

public class ${project_name}ContentProvider extends RestContentProvider {

	public static final String AUTHORITY = "${base_package}.providers.${project_name}ContentProvider";
	
	public static class Uris {
		% for name, model in model_lookup.items():
		public static final Uri ${common.pluralUnderscoreUppercase(name)}_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.${common.pluralUnderscoreUppercase(name)});
		%endfor
	}
	
	protected static class Paths {
		% for name, model in model_lookup.items():
		public static final String ${common.pluralUnderscoreUppercase(name)} = "${common.pluralUnderscoreLowercase(name)}";
		%endfor
	}
	
	@Override
	public boolean onCreate() {
		% for name, model in model_lookup.items():
        registerDataset(AUTHORITY, Paths.${common.pluralUnderscoreUppercase(name)}, ${name}Table.class, ${name}ListValidator.class);
		registerDataset(AUTHORITY, Paths.${common.pluralUnderscoreUppercase(name)} + "/*", ${name}Table.class, ${name}Validator.class);
		%endfor
		return true;
	}
}