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
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

import io.pivotal.arca.provider.ColumnName;
import io.pivotal.arca.provider.DataUtils;

public class DataUtilsTest extends AndroidTestCase {

    public void testDataUtilsValues() {
        final List<Model> models = new ArrayList<Model>();
        models.add(new Model("0", "test"));
        final ContentValues[] values = DataUtils.getContentValues(models);
        assertEquals(1, values.length);
        assertEquals("0", values[0].getAsString("id"));
        assertEquals("test", values[0].getAsString("title"));
    }

    public void testDataUtilsValuesCustom() {
        final List<Model> models = new ArrayList<Model>();
        models.add(new Model("0", "test"));
        final ContentValues custom = new ContentValues();
        custom.put("custom", "custom_test");
        final ContentValues[] values = DataUtils.getContentValues(models, custom);
        assertEquals(1, values.length);
        assertEquals("0", values[0].getAsString("id"));
        assertEquals("test", values[0].getAsString("title"));
        assertEquals("custom_test", values[0].getAsString("custom"));
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
}
