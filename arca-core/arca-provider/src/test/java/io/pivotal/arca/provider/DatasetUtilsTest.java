/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.provider;

import android.test.AndroidTestCase;

import io.pivotal.arca.provider.Column;
import io.pivotal.arca.provider.DatasetUtils;
import io.pivotal.arca.provider.GroupBy;
import io.pivotal.arca.provider.Joins;
import io.pivotal.arca.provider.OrderBy;
import io.pivotal.arca.provider.SQLiteTable;
import io.pivotal.arca.provider.SQLiteView;
import io.pivotal.arca.provider.Select;
import io.pivotal.arca.provider.SelectFrom;
import io.pivotal.arca.provider.Where;

public class DatasetUtilsTest extends AndroidTestCase {

	public void testDatasetUtilsDefaultTableConversion() {
		final String columns = DatasetUtils.getColumns(new TestTable());
		final String expected = "_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0";
		assertEquals(expected, columns);
	}

    public void testDatasetUtilsCustomTable1Conversion() {
        final String columns = DatasetUtils.getColumns(new TestTable1());
        final String expected = "test_id INTEGER,_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomTable2Conversion() {
        final String columns = DatasetUtils.getColumns(new TestTable2());
        final String expected = "custom TEXT,test_id INTEGER,_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomViewEmpty() {
        final String columns = DatasetUtils.getSelect(new TestView());
        assertEquals("", columns);
    }

    public void testDatasetUtilsCustomView1Conversion() {
        final String columns = DatasetUtils.getSelect(new TestView1());
        final String expected = "SELECT TestTable1.custom as custom FROM (TestTable1)";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomView2Conversion() {
        final String columns = DatasetUtils.getSelect(new TestView2());
        final String expected = "SELECT TestTable2.custom as custom FROM (TestTable1 LEFT JOIN TestTable2 ON TestTable1.test_id = TestTable2.test_id) GROUP BY TestTable1.test_id";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomView3Conversion() {
        final String columns = DatasetUtils.getSelect(new TestView3());
        final String expected = "SELECT TestTable2.custom as custom2,TestTable3.custom as custom3 FROM (TestTable1 LEFT JOIN TestTable2 ON TestTable1.test_id = TestTable2.test_id LEFT JOIN TestTable3 ON TestTable1.test_id = TestTable3.test_id)";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomView4Conversion() {
        final String columns = DatasetUtils.getSelect(new TestView4());
        final String expected = "SELECT * FROM (TestTable1) ORDER BY TestTable1.test_id";
        assertEquals(expected, columns);
    }

    public void testDatasetUtilsCustomView5Conversion() {
        final String columns = DatasetUtils.getSelect(new TestView5());
        final String expected = "SELECT * FROM (TestTable1) WHERE TestTable1.test_id = 0 ORDER BY TestTable1.test_id";
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

        public static interface Columns extends SQLiteTable.Columns {
            @Column(Column.Type.INTEGER)
            public static final String TEST_ID = "test_id";

            @Column(Column.Type.TEXT)
            public static final String CUSTOM = "custom";
        }
    }

    public static final class TestView extends SQLiteView {
    }

    public static final class TestView1 extends SQLiteView {

        @SelectFrom("TestTable1")

        public static interface Columns {
            @Select("TestTable1.custom")
            public static final String CUSTOM = "custom";
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