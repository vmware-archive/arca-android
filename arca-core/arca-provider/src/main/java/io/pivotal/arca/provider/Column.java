package io.pivotal.arca.provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    public static enum Type {
        INTEGER, REAL, TEXT, BLOB, NONE
    }

    Type value();
}
