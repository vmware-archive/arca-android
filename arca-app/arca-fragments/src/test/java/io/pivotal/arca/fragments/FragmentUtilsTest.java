/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.fragments;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.view.View;

import java.util.Arrays;
import java.util.Collection;

import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.ViewBinder;
import io.pivotal.arca.monitor.RequestMonitor;

public class FragmentUtilsTest extends AndroidTestCase {

    private static final int FRAGMENT_LAYOUT = 100;
    private static final int ADAPTER_ITEM_LAYOUT = 200;

    public void testFragmentLayoutAnnotation() {
        final int layout = FragmentUtils.getFragmentLayout(TestAllAnnotations.class);

        assertEquals(FRAGMENT_LAYOUT, layout);
    }

    public void testAdapterItemLayoutAnnotation() {
        final int layout = FragmentUtils.getAdapterItemLayout(TestAllAnnotations.class);

        assertEquals(ADAPTER_ITEM_LAYOUT, layout);
    }

    public void testAdapterItemLayoutAnnotationNotFound() {
        final int layout = FragmentUtils.getAdapterItemLayout(TestRequiredAnnotations.class);

        assertEquals(0, layout);
    }

    public void testMonitorAnnotation() {
        final RequestMonitor monitor = FragmentUtils.createRequestMonitor(TestAllAnnotations.class);
        final TestRequestMonitor requestMonitor = (TestRequestMonitor) monitor;

        assertNotNull(requestMonitor);
    }

    public void testMonitorAnnotationNotFound() {
        final RequestMonitor monitor = FragmentUtils.createRequestMonitor(TestRequiredAnnotations.class);

        assertNull(monitor);
    }

    public void testBinderAnnotation() {
        final ViewBinder binder = FragmentUtils.createViewBinder(TestAllAnnotations.class);
        final TestViewBinder viewBinder = (TestViewBinder) binder;

        assertNotNull(viewBinder);
    }

    public void testBinderAnnotationNotFound() {
        final ViewBinder binder = FragmentUtils.createViewBinder(TestRequiredAnnotations.class);

        assertNull(binder);
    }

    public void testBindingsAnnotation() {
        final Collection <Binding> bindings = FragmentUtils.getBindings(TestAllAnnotations.class);

        assertNotNull(bindings);
        assertEquals(3, bindings.size());
    }

    public void testBindingsAnnotationNotFound() {
        try {
            FragmentUtils.getBindings(TestRequiredAnnotations.class);
            fail();
        } catch (final Exception e) {
            assertNotNull(e);
        }
    }

    @ArcaFragment(
        fragmentLayout = FRAGMENT_LAYOUT,
        adapterItemLayout = ADAPTER_ITEM_LAYOUT,
        binder = TestViewBinder.class,
        monitor = TestRequestMonitor.class
    )
    private static final class TestAllAnnotations {

        @ArcaFragmentBindings
        private static final Collection<Binding> BINDINGS = Arrays.asList(
                new Binding(0, "0"), new Binding(1, "1"), new Binding(2, "2")
        );
    }

    @ArcaFragment(
        fragmentLayout = FRAGMENT_LAYOUT
    )
    private static final class TestRequiredAnnotations {

    }

    public static final class TestViewBinder implements ViewBinder{
        @Override
        public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
            return false;
        }
    }

    public static final class TestRequestMonitor extends RequestMonitor.AbstractRequestMonitor {

    }
}
