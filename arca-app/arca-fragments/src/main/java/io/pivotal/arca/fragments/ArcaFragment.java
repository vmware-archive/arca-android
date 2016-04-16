package io.pivotal.arca.fragments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.pivotal.arca.adapters.ViewBinder;
import io.pivotal.arca.monitor.RequestMonitor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArcaFragment {

    int fragmentLayout();
    int adapterItemLayout() default 0;

    Class<? extends ViewBinder> binder() default ViewBinder.class;
    Class<? extends RequestMonitor> monitor() default RequestMonitor.class;
}
