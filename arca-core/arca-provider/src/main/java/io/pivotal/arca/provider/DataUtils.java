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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import io.pivotal.arca.utils.Logger;

public class DataUtils {

    public static ContentValues[] getContentValues(final List<?> list) {
        return getContentValues(list, null);
    }

    public static ContentValues[] getContentValues(final List<?> list, final ContentValues custom) {
        final ContentValues[] values = new ContentValues[list.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = getContentValues(list.get(i));
            if (custom != null) {
                values[i].putAll(custom);
            }
        }
        return values;
    }

    public static ContentValues getContentValues(final Object object) {
        final Class<?> klass = object.getClass();
        final ContentValues values = new ContentValues();
        try {
            getContentValuesFromFields(object, klass, values);
            getContentValuesFromMethods(object, klass, values);
        } catch (final Exception e) {
            Logger.ex(e);
        }
        return values;
    }

    private static void getContentValuesFromMethods(final Object object, final Class<?> klass, final ContentValues values) throws Exception {
        final Method[] methods = klass.getDeclaredMethods();
        for (final Method method : methods) {
            final ColumnName annotation = method.getAnnotation(ColumnName.class);
            if (annotation != null) {
                method.setAccessible(true);
                final Object value = method.invoke(object);
                addToValues(values, annotation.value(), value);
            }
        }

        final Class<?> superKlass = klass.getSuperclass();
        if (superKlass != null) {
            getContentValuesFromMethods(object, superKlass, values);
        }
    }

    private static void getContentValuesFromFields(final Object object, final Class<?> klass, final ContentValues values) throws Exception {
        final Field[] fields = klass.getDeclaredFields();
        for (final Field field : fields) {
            final ColumnName annotation = field.getAnnotation(ColumnName.class);
            if (annotation != null) {
                field.setAccessible(true);
                final Object value = field.get(object);
                addToValues(values, annotation.value(), value);
            }
        }

        final Class<?> superKlass = klass.getSuperclass();
        if (superKlass != null) {
            getContentValuesFromFields(object, superKlass, values);
        }
    }

    private static void addToValues(final ContentValues values, final String key, final Object value) throws Exception {
        if (value != null) {
            final Class<?> klass = values.getClass();
            final Method method = klass.getMethod("put", String.class, value.getClass());
            method.invoke(values, key, value);
        }
    }
}
