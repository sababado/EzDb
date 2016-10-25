package com.sababado.ezdb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by robert on 9/15/15.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    public static final String FK_COL_NAME = "___fk";
    public static final String ID = "id";

    String value();

    boolean ignoreInQueryGenerator() default false;
}
