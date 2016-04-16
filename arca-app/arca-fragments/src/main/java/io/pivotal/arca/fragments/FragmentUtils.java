package io.pivotal.arca.fragments;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.ViewBinder;
import io.pivotal.arca.monitor.RequestMonitor;
import io.pivotal.arca.utils.Logger;

public class FragmentUtils {

    public static int getFragmentLayout(final Class<?> klass) {
        final ArcaFragment annotation = klass.getAnnotation(ArcaFragment.class);
        if (annotation != null) {
            return annotation.fragmentLayout();
        } else {
            throw new IllegalStateException("@ArcaFragment annotation not found.");
        }
    }

    public static int getAdapterItemLayout(final Class<?> klass) {
        final ArcaFragment annotation = klass.getAnnotation(ArcaFragment.class);
        if (annotation != null) {
            return annotation.adapterItemLayout();
        } else {
            throw new IllegalStateException("@ArcaFragment annotation not found.");
        }
    }

    public static RequestMonitor createRequestMonitor(final Class<?> klass) {
        final ArcaFragment annotation = klass.getAnnotation(ArcaFragment.class);
        if (annotation != null) {
            final Class<? extends RequestMonitor> monitor = annotation.monitor();
            try {
                return monitor.newInstance();
            } catch (final Exception e) {
                return null;
            }
        } else {
            throw new IllegalStateException("@ArcaFragment annotation not found.");
        }
    }

    public static ViewBinder createViewBinder(final Class<?> klass) {
        final ArcaFragment annotation = klass.getAnnotation(ArcaFragment.class);
        if (annotation != null) {
            final Class<? extends ViewBinder> binder = annotation.binder();
            try {
                return binder.newInstance();
            } catch (final Exception e) {
                return null;
            }
        } else {
            throw new IllegalStateException("@ArcaFragment annotation not found.");
        }
    }

    public static Collection<Binding> getBindings(final Class<?> klass) {
        final Field[] fields = klass.getDeclaredFields();
        for (final Field field : fields) {
            final ArcaFragmentBindings annotation = field.getAnnotation(ArcaFragmentBindings.class);
            if (annotation != null && isBindingsField(field)) {
                return getBindings(klass, field);
            }
        }
        throw new IllegalStateException("@ArcaFragmentBindings annotation not found.");
    }

    private static boolean isBindingsField(final Field field) {
        final Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType) {
            final ParameterizedType paramType = (ParameterizedType) genericType;
            final Class<?> rawType = (Class<?>) paramType.getRawType();
            final Class<?> actualType = (Class<?>) paramType.getActualTypeArguments()[0];

            final boolean isCollection = rawType.isAssignableFrom(Collection.class);
            final boolean hasBindings = actualType.isAssignableFrom(Binding.class);

            if (isCollection && hasBindings) {
                return true;
            }
        }

        throw new IllegalStateException("@ArcaFragmentBindings annotation must be applied to Collection<Binding>.");
    }

    @SuppressWarnings("unchecked")
    private static Collection<Binding> getBindings(final Class<?> klass, final Field field) {
        try {
            field.setAccessible(true);
            return (Collection<Binding>) field.get(null);
        } catch (final Exception e) {
            Logger.ex(e);
            return null;
        }
    }
}

