package io.pivotal.arca.provider;

import android.test.AndroidTestCase;

public class DatasetUtilsTest extends AndroidTestCase {

	public void testDatasetUtilsDefaultTableConversion() {
		final String columns = DatasetUtils.getTableDefinition(new TestTable());
		final String expected = "(_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0)";
		assertEquals(expected, columns);
	}

    public void testDatasetUtilsCustomTable1Conversion() {
        final String columns = DatasetUtils.getTableDefinition(new TestTable1());
        final String expected = "(test_id INTEGER,_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0)";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomTable2Conversion() {
        final String columns = DatasetUtils.getTableDefinition(new TestTable2());
        final String expected = "(custom TEXT,test_id INTEGER,_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0)";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomTable3Conversion() {
        final String columns = DatasetUtils.getTableDefinition(new TestTable3());
        final String expected = "AS SELECT 1 as custom,TestTable1.test_id as test_id FROM (TestTable1)";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomTable4Conversion() {
        final String columns = DatasetUtils.getTableDefinition(new TestTable4());
        final String expected = "AS SELECT TestTable2.custom as custom FROM (TestTable1 LEFT JOIN TestTable2 ON TestTable1.test_id = TestTable2.test_id) GROUP BY TestTable1.test_id";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomViewEmpty() {
        final String columns = DatasetUtils.getViewDefinition(new TestView());
        assertEquals("", columns);
    }

    public void testDatasetUtilsCustomView1Conversion() {
        final String columns = DatasetUtils.getViewDefinition(new TestView1());
        final String expected = "AS SELECT TestTable1.test_id as test_id FROM (TestTable1)";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomView2Conversion() {
        final String columns = DatasetUtils.getViewDefinition(new TestView2());
        final String expected = "AS SELECT TestTable2.custom as custom FROM (TestTable1 LEFT JOIN TestTable2 ON TestTable1.test_id = TestTable2.test_id) GROUP BY TestTable1.test_id";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomView3Conversion() {
        final String columns = DatasetUtils.getViewDefinition(new TestView3());
        final String expected = "AS SELECT TestTable2.custom as custom2,TestTable3.custom as custom3 FROM (TestTable1 LEFT JOIN TestTable2 ON TestTable1.test_id = TestTable2.test_id LEFT JOIN TestTable3 ON TestTable1.test_id = TestTable3.test_id)";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomView4Conversion() {
        final String columns = DatasetUtils.getViewDefinition(new TestView4());
        final String expected = "AS SELECT * FROM (TestTable1) ORDER BY TestTable1.test_id";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomView5Conversion() {
        final String columns = DatasetUtils.getViewDefinition(new TestView5());
        final String expected = "AS SELECT * FROM (TestTable1) WHERE TestTable1.test_id = 0 ORDER BY TestTable1.test_id";
        assertEquals(expected, columns);
    }

	// ====================================

    public static final class TestTable extends SQLiteTable {

        public static interface Columns extends SQLiteTable.Columns {
        }
    }

	public static final class TestTable1 extends SQLiteTable {

		public static interface Columns extends SQLiteTable.Columns {
            @Column(Column.Type.INTEGER)
            public static final String TEST_ID = "test_id";
		}
	}

    public static final class TestTable2 extends SQLiteTable {

        public static interface Columns extends SQLiteTable.Columns {
            @Column(Column.Type.INTEGER)
            public static final String TEST_ID = "test_id";

            @Column(Column.Type.TEXT)
            public static final String CUSTOM = "custom";
        }
    }

    public static final class TestTable3 extends SQLiteTable {

        @SelectFrom("TestTable1")

        public static interface Columns {
            @Select("TestTable1.test_id")
            public static final String TEST_ID = "test_id";

            @Select("1")
            public static final String CUSTOM = "custom";
        }
    }

    public static final class TestTable4 extends SQLiteTable {

        @SelectFrom("TestTable1")

        @Joins({
            "LEFT JOIN TestTable2 ON TestTable1.test_id = TestTable2.test_id"
        })

        @GroupBy("TestTable1.test_id")

        public static interface Columns {
            @Select("TestTable2.custom")
            public static final String CUSTOM = "custom";
        }
    }

    public static final class TestView extends SQLiteView {
    }

    public static final class TestView1 extends SQLiteView {

        @SelectFrom("TestTable1")

        public static interface Columns {
            @Select("TestTable1.test_id")
            public static final String TEST_ID = "test_id";
        }
    }

    public static final class TestView2 extends SQLiteView {

        @SelectFrom("TestTable1")

        @Joins({
            "LEFT JOIN TestTable2 ON TestTable1.test_id = TestTable2.test_id"
        })

        @GroupBy("TestTable1.test_id")

        public static interface Columns {
            @Select("TestTable2.custom")
            public static final String CUSTOM = "custom";
        }
    }

    public static final class TestView3 extends SQLiteView {

        @SelectFrom("TestTable1")

        @Joins({
            "LEFT JOIN TestTable2 ON TestTable1.test_id = TestTable2.test_id",
            "LEFT JOIN TestTable3 ON TestTable1.test_id = TestTable3.test_id"
        })

        public static interface Columns {
            @Select("TestTable2.custom")
            public static final String CUSTOM2 = "custom2";

            @Select("TestTable3.custom")
            public static final String CUSTOM3 = "custom3";
        }
    }

    public static final class TestView4 extends SQLiteView {

        @SelectFrom("TestTable1")

        @OrderBy("TestTable1.test_id")

        public static interface Columns {}
    }

    public static final class TestView5 extends SQLiteView {

        @SelectFrom("TestTable1")

        @Where("TestTable1.test_id = 0")

        @OrderBy("TestTable1.test_id")

        public static interface Columns {}
    }
}
