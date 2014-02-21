package ${base_package}.datasets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xtreme.rest.utils.ArrayUtils;
import com.xtreme.rest.utils.StringUtils;

public class ${name}Table extends Abs${name}Table {
	
	private static final class Holder { 
        public static final ${name}Table INSTANCE = new ${name}Table();
	}
	
	public static ${name}Table getInstance() {
	    return Holder.INSTANCE;
	}
	
	public static final class Columns extends Abs${name}Table.Columns {}
	
	private ${name}Table() {}
	
	@Override
	protected String onCreateUniqueConstraint() {
		return "UNIQUE (" + Columns.${common.idProp(properties)} + ") ON CONFLICT REPLACE";
	}
	
	@Override
	public Cursor query(final SQLiteDatabase database, final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		if (uri.getPathSegments().size() > 1) { 
			 final String selectionWithId = StringUtils.append(selection, Columns.${common.idProp(properties)} + "=?", " AND ");
			final String[] selectionArgsWithId = ArrayUtils.append(selectionArgs, new String[] { uri.getLastPathSegment() });
			return super.query(database, uri, projection, selectionWithId, selectionArgsWithId, sortOrder);
		} else {
			return super.query(database, uri, projection, selection, selectionArgs, sortOrder);
		}
	}

}

