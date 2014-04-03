package com.arca.provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

	public static enum OnConflict {
		ROLLBACK, ABORT, FAIL, IGNORE, REPLACE
	}
	
	OnConflict value() default OnConflict.REPLACE;

}