package com.appnet.app.datasets;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.appnet.app.models.Post;
import com.xtreme.rest.utils.ArrayUtils;
import com.xtreme.rest.utils.StringUtils;

public class PostTable extends AbsPostTable {
	
	private static final class Holder { 
        public static final PostTable INSTANCE = new PostTable();
	}
	
	public static synchronized PostTable getInstance() {
	    return Holder.INSTANCE;
	}
	
	public static final class Columns extends AbsPostTable.Columns {
		public static final String IMAGE_URL = "image_url";
	}
	
	private PostTable() {}

	@Override
	protected Map<String, String> onCreateColumnMapping() {
	    final Map<String, String> map = super.onCreateColumnMapping();
	    map.put(Columns.IMAGE_URL, "TEXT");
	    return map;
	}

	@Override
	public ContentValues getContentValues(final Post item) {
	    final ContentValues value = super.getContentValues(item);
	    value.put(Columns.IMAGE_URL, item.getImageUrl());
	    return value;
	}
	
	@Override
	protected String onCreateUniqueConstraint() {
		return "UNIQUE (" + Columns.ID + ") ON CONFLICT REPLACE";
	}
	
	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		if (uri.getPathSegments().size() > 1) { 
			final String selectionWithId = StringUtils.append(selection, Columns.ID + "=?", " AND ");
			final String[] selectionArgsWithId = ArrayUtils.append(selectionArgs, new String[] { uri.getLastPathSegment() });
			return super.query(uri, projection, selectionWithId, selectionArgsWithId, sortOrder);
		} else {
			return super.query(uri, projection, selection, selectionArgs, sortOrder);
		}
	}

}

