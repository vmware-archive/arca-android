package com.appnet.app.datasets;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.appnet.app.models.Post;
import com.xtreme.rest.provider.Column;
import com.xtreme.rest.provider.Column.Type;
import com.xtreme.rest.provider.ColumnUtils;
import com.xtreme.rest.provider.SQLTable;
import com.xtreme.rest.utils.ArrayUtils;
import com.xtreme.rest.utils.StringUtils;

public class PostTable extends SQLTable {
	
	public static interface Columns extends SQLTable.Columns {
        public static final Column ID = Type.TEXT.newColumn("id");
        public static final Column CREATED_AT = Type.TEXT.newColumn("created_at");
        public static final Column TEXT = Type.TEXT.newColumn("text");
        public static final Column NUM_STARS = Type.TEXT.newColumn("num_stars");
        public static final Column NUM_REPOSTS = Type.TEXT.newColumn("num_reposts");
        public static final Column NUM_REPLIES = Type.TEXT.newColumn("num_replies");
        public static final Column IMAGE_URL = Type.TEXT.newColumn("image_url");
	}
	
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String columns = ColumnUtils.toString(Columns.class);
		final String constraint = "UNIQUE (" + Columns.ID + ") ON CONFLICT REPLACE";
		db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s, %s);", getName(), columns, constraint));
	}
	
	@Override
	public void onDrop(final SQLiteDatabase db) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getName()));
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
	
	public static ContentValues[] getContentValues(final List<Post> list) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i));
		}
		return values;
    }
	
	public static ContentValues getContentValues(final Post item) {
		final ContentValues value = new ContentValues();
        value.put(Columns.ID.name, item.getId());
        value.put(Columns.CREATED_AT.name, item.getCreatedAt());
        value.put(Columns.TEXT.name, item.getText());
        value.put(Columns.NUM_STARS.name, item.getNumStars());
        value.put(Columns.NUM_REPOSTS.name, item.getNumReposts());
        value.put(Columns.NUM_REPLIES.name, item.getNumReplies());
        value.put(Columns.IMAGE_URL.name, item.getImageUrl());
        return value;
    }

}

