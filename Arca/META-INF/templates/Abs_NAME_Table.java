package ${base_package}.datasets;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.provider.BaseColumns;

import ${base_package}.models.${name};

import com.xtreme.rest.providers.SQLTable;

public abstract class Abs${name}Table extends SQLTable {
	
	public static final String TABLE_NAME = "${common.pluralUnderscoreLowercase(name)}";
	
	protected static class Columns {
		%for prop in properties: 
			%if common.isSqliteType(prop, sqlite_types):
        public static final String ${common.underscoreUppercase(prop['key'])} = ${name}.Fields.${common.underscoreUppercase(prop['key'])};
			%endif
		%endfor
	}
	
	@Override
	public String getName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> onCreateColumnMapping() {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(BaseColumns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		%for prop in properties: 
			%if common.isSqliteType(prop, sqlite_types):
        map.put(Columns.${common.underscoreUppercase(prop['key'])}, "${sqlite_types[prop['type']].upper()}");
			%endif
		%endfor
		return map;
	}
	
	public ContentValues[] getContentValues(final List<${name}> list) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i));
		}
		return values;
    }
	
	public ContentValues getContentValues(final ${name} item) {
		final ContentValues value = new ContentValues();
		%for prop in properties:
			%if common.isSqliteType(prop, sqlite_types):
        value.put(Columns.${common.underscoreUppercase(prop['key'])}, item.get${common.capitalize(prop['name'])}());
			%endif
    	%endfor
        return value;
    }
	
}
