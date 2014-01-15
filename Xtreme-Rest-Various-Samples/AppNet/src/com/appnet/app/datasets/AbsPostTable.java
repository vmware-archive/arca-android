package com.appnet.app.datasets;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.appnet.app.models.Post;
import com.xtreme.rest.provider.SQLTable;

public abstract class AbsPostTable extends SQLTable {
	
	public static final String TABLE_NAME = "posts";
	
	protected static class Columns {
        public static final String ID = Post.Fields.ID;
        public static final String CREATED_AT = Post.Fields.CREATED_AT;
        public static final String TEXT = Post.Fields.TEXT;
        public static final String NUM_STARS = Post.Fields.NUM_STARS;
        public static final String NUM_REPOSTS = Post.Fields.NUM_REPOSTS;
        public static final String NUM_REPLIES = Post.Fields.NUM_REPLIES;
	}
	
	@Override
	public String getName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> onCreateColumnMapping() {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(BaseColumns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
        map.put(Columns.ID, "TEXT");
        map.put(Columns.CREATED_AT, "TEXT");
        map.put(Columns.TEXT, "TEXT");
        map.put(Columns.NUM_STARS, "TEXT");
        map.put(Columns.NUM_REPOSTS, "TEXT");
        map.put(Columns.NUM_REPLIES, "TEXT");
		return map;
	}
	
	public ContentValues[] getContentValues(final List<Post> list) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i));
		}
		return values;
    }
	
	public ContentValues getContentValues(final Post item) {
		final ContentValues value = new ContentValues();
        value.put(Columns.ID, item.getId());
        value.put(Columns.CREATED_AT, item.getCreatedAt());
        value.put(Columns.TEXT, item.getText());
        value.put(Columns.NUM_STARS, item.getNumStars());
        value.put(Columns.NUM_REPOSTS, item.getNumReposts());
        value.put(Columns.NUM_REPLIES, item.getNumReplies());
        return value;
    }
	
}
