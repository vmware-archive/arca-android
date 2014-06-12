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
package io.pivotal.arca.provider.test;

import android.content.ContentValues;
import android.database.MatrixCursor;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.pivotal.arca.provider.ColumnName;
import io.pivotal.arca.provider.DataUtils;

public class DataUtilsTest extends AndroidTestCase {

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
        cursor.addRow(new Object[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null});

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
