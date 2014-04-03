package com.rottentomatoes.app.datasets;

import com.arca.provider.Column;
import com.arca.provider.Column.Type;
import com.arca.provider.SQLiteTable;
import com.arca.provider.Unique;

public class MovieTypeTable extends SQLiteTable {
	
	public static interface ColumnNames {
		public static final String ID = "id";
		public static final String TYPE = "type";
	}
	
	public static interface Columns extends SQLiteTable.Columns {
		@Unique public static final Column ID = Type.TEXT.newColumn(ColumnNames.ID);
		@Unique public static final Column TYPE = Type.TEXT.newColumn(ColumnNames.TYPE);
	}
}

