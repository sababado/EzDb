package com.sababado.ezdb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by robert on 9/15/15.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {
    String value();

    /**
     * This can be a comma separated string to support joining on multiple tables.
     *
     * @return A comma separated string denoting multiple join tables.
     */
    String joinTable() default "";

    /**
     * If defined then this will be prefixed to all column names in queries.
     * @return
     */
    String alias() default "";
}
