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

import android.content.ContentValues;
import android.database.MatrixCursor;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.pivotal.arca.provider.Column;
import io.pivotal.arca.provider.ColumnName;
import io.pivotal.arca.provider.DataUtils;

public class DataUtilsTest extends AndroidTestCase {

    private static final class TestTable {
        public static final class Columns {
            @Column(Column.Type.TEXT)
            public static final String STRING = "string";

            @Column(Column.Type.BLOB)
            public static final String BLOB = "blob";

            @Column(Column.Type.INTEGER)
            public static final String INTEGER = "integer1";

            @Column(Column.Type.REAL)
            public static final String FLOAT = "float1";
        }
    }

    public void testDataUtilsConvertsEmptyCursorToContentValues() {
        final MatrixCursor cursor = new MatrixCursor(new String[]{
            "string", "blob", "integer1", "float1"
        });

        final ContentValues[] values = DataUtils.getContentValues(cursor, TestTable.class);

        assertEquals(0, values.length);
    }

    public void testDataUtilsConvertsCursorToContentValues() {
        final MatrixCursor cursor = new MatrixCursor(new String[]{
            "string", "blob", "integer1", "integer2", "boolean1", "boolean2", "float1", "float2", "long1", "long2", "double1", "double2","short1", "short2", "character1", "character2", "byte1", "byte2"
        });

        cursor.addRow(new Object[]{ "string0", "blob0".getBytes(), 0, 0, 0, 0, 0.0f, 0.0f, 0L, 0L, 0.0d, 0.0d, (short)0, (short)0, '0', '0', (byte)0, (byte)0 });
        cursor.addRow(new Object[]{ "string1", "blob1".getBytes(), 1, 1, 1, 1, 1.0f, 1.0f, 1L, 1L, 1.0d, 1.0d, (short)1, (short)1, '1', '1', (byte)1, (byte)1 });
        cursor.addRow(new Object[]{ null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });

        final ContentValues[] values = DataUtils.getContentValues(cursor, TestTable.class);

        final ContentValues values0 = values[0];
        final ContentValues values1 = values[1];
        final ContentValues values2 = values[2];

        assertEquals(3, values.length);

        assertEquals("string0", values0.getAsString("string"));
        assertTrue(Arrays.equals("blob0".getBytes(), values0.getAsByteArray("blob")));
        assertEquals(0, values0.getAsInteger("integer1").intValue());
        assertEquals(0.0f, values0.getAsFloat("float1").floatValue());

        assertEquals("string1", values1.getAsString("string"));
        assertTrue(Arrays.equals("blob1".getBytes(), values1.getAsByteArray("blob")));
        assertEquals(1, values1.getAsInteger("integer1").intValue());
        assertEquals(1.0f, values1.getAsFloat("float1").floatValue());

        assertEquals(null, values2.getAsString("string"));
        assertEquals(null, values2.getAsByteArray("blob"));
        assertEquals(0, values2.getAsInteger("integer1").intValue());
        assertEquals(0.0f, values2.getAsFloat("float1").floatValue());
    }


    public void testDataUtilsConvertsEmptyListToContentValues() {
        final List<Model> models = new ArrayList<Model>();
        final ContentValues[] values = DataUtils.getContentValues(models);
        assertEquals(0, values.length);
    }

    public void testDataUtilsConvertsListToContentValues() {
        final List<Model> models = new ArrayList<Model>();
        models.add(new Model("0", "test"));

        final ContentValues[] values = DataUtils.getContentValues(models);

        assertEquals(1, values.length);
        assertEquals("0", values[0].getAsString("id"));
        assertEquals("test", values[0].getAsString("title"));
    }

    public void testDataUtilsConvertsListToContentValuesWithCustomValues() {
        final List<Model> models = new ArrayList<Model>();
        models.add(new Model("0", "test"));

        final ContentValues custom = new ContentValues();
        custom.put("custom", "custom_test");

        final ContentValues[] values = DataUtils.getContentValues(models, custom);

        assertEquals(1, values.length);
        assertEquals("custom_test", values[0].getAsString("custom"));
    }

    public void testDataUtilsConvertsCursorToList() {
        final MatrixCursor cursor = new MatrixCursor(new String[]{
            "string", "blob", "integer1", "integer2", "boolean1", "boolean2", "float1", "float2", "long1", "long2", "double1", "double2","short1", "short2", "character1", "character2", "byte1", "byte2"
        });

        cursor.addRow(new Object[]{ "string0", "blob0".getBytes(), 0, 0, 0, 0, 0.0f, 0.0f, 0L, 0L, 0.0d, 0.0d, (short)0, (short)0, '0', '0', (byte)0, (byte)0 });
        cursor.addRow(new Object[]{ "string1", "blob1".getBytes(), 1, 1, 1, 1, 1.0f, 1.0f, 1L, 1L, 1.0d, 1.0d, (short)1, (short)1, '1', '1', (byte)1, (byte)1 });
        cursor.addRow(new Object[]{ null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });

        final List<TypeModel> models = DataUtils.getList(cursor, TypeModel.class);

        assertEquals(3, models.size());

        final TypeModel model0 = models.get(0);
        final TypeModel model1 = models.get(1);
        final TypeModel model2 = models.get(2);

        assertEquals("string0", model0.mString);
        assertTrue(Arrays.equals("blob0".getBytes(), model0.mBlob));
        assertEquals(0, model0.mInteger1);
        assertEquals(0, model0.mInteger2.intValue());
        assertFalse(model0.mBoolean1);
        assertFalse(model0.mBoolean2.booleanValue());
        assertEquals(0.0f, model0.mFloat1);
        assertEquals(0.0f, model0.mFloat2.floatValue());
        assertEquals(0L, model0.mLong1);
        assertEquals(0L, model0.mLong2.longValue());
        assertEquals(0.0d, model0.mDouble1);
        assertEquals(0.0d, model0.mDouble2.doubleValue());
        assertEquals((short)0,  model0.mShort1);
        assertEquals((short)0,  model0.mShort2.shortValue());
        assertEquals('0',  model0.mCharacter1);
        assertEquals('0',  model0.mCharacter2.charValue());
        assertEquals((byte)0,  model0.mByte1);
        assertEquals((byte)0,  model0.mByte2.shortValue());

        assertEquals("string1", model1.mString);
        assertTrue(Arrays.equals("blob1".getBytes(), model1.mBlob));
        assertEquals(1, model1.mInteger1);
        assertEquals(1, model1.mInteger2.intValue());
        assertTrue(model1.mBoolean1);
        assertTrue(model1.mBoolean2.booleanValue());
        assertEquals(1.0f, model1.mFloat1);
        assertEquals(1.0f, model1.mFloat2.floatValue());
        assertEquals(1L, model1.mLong1);
        assertEquals(1L, model1.mLong2.longValue());
        assertEquals(1.0d, model1.mDouble1);
        assertEquals(1.0d, model1.mDouble2.doubleValue());
        assertEquals((short)1,  model1.mShort1);
        assertEquals((short)1,  model1.mShort2.shortValue());
        assertEquals('1',  model1.mCharacter1);
        assertEquals('1',  model1.mCharacter2.charValue());
        assertEquals((byte)1,  model1.mByte1);
        assertEquals((byte)1,  model1.mByte2.shortValue());

        assertEquals(null, model2.mString);
        assertEquals(null, model2.mBlob);
        assertEquals(0, model2.mInteger1);
        assertEquals(0, model2.mInteger2.intValue());
        assertFalse(model2.mBoolean1);
        assertFalse(model2.mBoolean2.booleanValue());
        assertEquals(0.0f, model2.mFloat1);
        assertEquals(0.0f, model2.mFloat2.floatValue());
        assertEquals(0L, model2.mLong1);
        assertEquals(0L, model2.mLong2.longValue());
        assertEquals(0.0d, model2.mDouble1);
        assertEquals(0.0d, model2.mDouble2.doubleValue());
        assertEquals((short)0,  model2.mShort1);
        assertEquals((short)0,  model2.mShort2.shortValue());
        assertEquals('\0',  model2.mCharacter1);
        assertEquals('\0',  model2.mCharacter2.charValue());
        assertEquals((byte)0,  model2.mByte1);
        assertEquals((byte)0,  model2.mByte2.shortValue());
    }

    public void testDataUtilsConvertsContentValuesArrayToList() {

        final ContentValues[] values = new ContentValues[3];
        values[0] = new ContentValues();
        values[0].put("string", "string0");
        values[0].put("blob", "blob0".getBytes());
        values[0].put("integer1", 0);
        values[0].put("integer2", new Integer(0));
        values[0].put("boolean1", false);
        values[0].put("boolean2", Boolean.FALSE);
        values[0].put("float1", 0.0f);
        values[0].put("float2", new Float(0));
        values[0].put("long1", 0L);
        values[0].put("long2", new Long(0));
        values[0].put("double1", 0.0d);
        values[0].put("double2", new Double(0));
        values[0].put("short1", (short)0);
        values[0].put("short2", new Short((short)0));
        values[0].put("character1", "0");
        values[0].put("character2", new String("0"));
        values[0].put("byte1", (byte)0);
        values[0].put("byte2", new Byte((byte)0));

        values[1] = new ContentValues();
        values[1].put("string", "string1");
        values[1].put("blob", "blob1".getBytes());
        values[1].put("integer1", 1);
        values[1].put("integer2", new Integer(1));
        values[1].put("boolean1", true);
        values[1].put("boolean2", Boolean.TRUE);
        values[1].put("float1", 1.0f);
        values[1].put("float2", new Float(1));
        values[1].put("long1", 1L);
        values[1].put("long2", new Long(1));
        values[1].put("double1", 1.0d);
        values[1].put("double2", new Double(1));
        values[1].put("short1", (short)1);
        values[1].put("short2", new Short((short)1));
        values[1].put("character1", "1");
        values[1].put("character2", new String("1"));
        values[1].put("byte1", (byte)1);
        values[1].put("byte2", new Byte((byte)1));

        values[2] = new ContentValues();

        final List<TypeModel> models = DataUtils.getList(values, TypeModel.class);

        assertEquals(3, models.size());

        final TypeModel model0 = models.get(0);
        final TypeModel model1 = models.get(1);
        final TypeModel model2 = models.get(2);

        assertEquals("string0", model0.mString);
        assertTrue(Arrays.equals("blob0".getBytes(), model0.mBlob));
        assertEquals(0, model0.mInteger1);
        assertEquals(0, model0.mInteger2.intValue());
        assertFalse(model0.mBoolean1);
        assertFalse(model0.mBoolean2.booleanValue());
        assertEquals(0.0f, model0.mFloat1);
        assertEquals(0.0f, model0.mFloat2.floatValue());
        assertEquals(0L, model0.mLong1);
        assertEquals(0L, model0.mLong2.longValue());
        assertEquals(0.0d, model0.mDouble1);
        assertEquals(0.0d, model0.mDouble2.doubleValue());
        assertEquals((short)0,  model0.mShort1);
        assertEquals((short)0,  model0.mShort2.shortValue());
        assertEquals('0',  model0.mCharacter1);
        assertEquals('0',  model0.mCharacter2.charValue());
        assertEquals((byte)0,  model0.mByte1);
        assertEquals((byte)0,  model0.mByte2.shortValue());

        assertEquals("string1", model1.mString);
        assertTrue(Arrays.equals("blob1".getBytes(), model1.mBlob));
        assertEquals(1, model1.mInteger1);
        assertEquals(1, model1.mInteger2.intValue());
        assertTrue(model1.mBoolean1);
        assertTrue(model1.mBoolean2.booleanValue());
        assertEquals(1.0f, model1.mFloat1);
        assertEquals(1.0f, model1.mFloat2.floatValue());
        assertEquals(1L, model1.mLong1);
        assertEquals(1L, model1.mLong2.longValue());
        assertEquals(1.0d, model1.mDouble1);
        assertEquals(1.0d, model1.mDouble2.doubleValue());
        assertEquals((short)1,  model1.mShort1);
        assertEquals((short)1,  model1.mShort2.shortValue());
        assertEquals('1',  model1.mCharacter1);
        assertEquals('1',  model1.mCharacter2.charValue());
        assertEquals((byte)1,  model1.mByte1);
        assertEquals((byte)1,  model1.mByte2.shortValue());

        assertEquals(null, model2.mString);
        assertEquals(null, model2.mBlob);
        assertEquals(0, model2.mInteger1);
        assertEquals(null, model2.mInteger2);
        assertFalse(model2.mBoolean1);
        assertEquals(null, model2.mBoolean2);
        assertEquals(0.0f, model2.mFloat1);
        assertEquals(null, model2.mFloat2);
        assertEquals(0L, model2.mLong1);
        assertEquals(null, model2.mLong2);
        assertEquals(0.0d, model2.mDouble1);
        assertEquals(null, model2.mDouble2);
        assertEquals((short)0,  model2.mShort1);
        assertEquals(null,  model2.mShort2);
        assertEquals('\0',  model2.mCharacter1);
        assertEquals(null,  model2.mCharacter2);
        assertEquals((byte)0,  model2.mByte1);
        assertEquals(null,  model2.mByte2);
    }

    private static final class Model {

        @ColumnName("id")
        private String mId;

        @ColumnName("title")
        private String mTitle;

        private Model(final String id, final String title) {
            mId = id;
            mTitle = title;
        }
    }

    public static final class TypeModel {

        @ColumnName("string")
        public String mString;

        @ColumnName("blob")
        public byte[] mBlob;

        @ColumnName("integer1")
        public int mInteger1;

        @ColumnName("integer2")
        public Integer mInteger2;

        @ColumnName("boolean1")
        public boolean mBoolean1;

        @ColumnName("boolean2")
        public Boolean mBoolean2;

        @ColumnName("float1")
        public float mFloat1;

        @ColumnName("float2")
        public Float mFloat2;

        @ColumnName("long1")
        public long mLong1;

        @ColumnName("long2")
        public Long mLong2;

        @ColumnName("double1")
        public double mDouble1;

        @ColumnName("double2")
        public Double mDouble2;

        @ColumnName("short1")
        public short mShort1;

        @ColumnName("short2")
        public Short mShort2;

        @ColumnName("character1")
        public char mCharacter1;

        @ColumnName("character2")
        public Character mCharacter2;

        @ColumnName("byte1")
        public byte mByte1;

        @ColumnName("byte2")
        public Byte mByte2;
    }
}
