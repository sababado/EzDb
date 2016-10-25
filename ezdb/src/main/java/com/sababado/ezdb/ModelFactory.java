package com.sababado.ezdb;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;

/**
 * Created by robert on 9/15/15.
 */
public class ModelFactory {
    public static <T> T newInstance(Class<T> cls, ResultSet resultSet) {
        try {
            return cls.getDeclaredConstructor(ResultSet.class).newInstance(resultSet);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
