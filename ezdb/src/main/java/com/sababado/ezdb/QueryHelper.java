package com.sababado.ezdb;

import java.util.logging.Logger;

/**
 * Created by robert on 9/16/15.
 */
public abstract class QueryHelper {
    protected static final Logger _logger = Logger.getLogger(DbHelper.class.getName());

    static String appendValue(String where, String columnName, DbRecord dbRecord) {
        if (dbRecord != null) {
            where = where == null ? "where" : where + " and";
            where += String.format(" %s = %d", columnName, dbRecord.getId());
        }
        return where;
    }

    static String appendValue(String where, String columnName, String columnValue, boolean isString) {
        assert columnValue != null;
        where = where == null ? "where" : where + " and";
        String clause = isString ? " %s = '%s'" : " %s = %s";
        where += String.format(clause, columnName, columnValue);
        return where;
    }

    static String limitOne(String where) {
        if (where == null) {
            where = "";
        }
        return where + " limit 1";
    }

    protected static String buildWhereQuery(String[] columnNames, Object[] values, boolean limitOne) {
        assert columnNames.length == values.length;
        if (columnNames.length < 1) {
            return null;
        }
        String where = null;
        for (int i = 0; i < columnNames.length; i++) {
            Object obj = values[i];
            if (obj != null) {
                String value = String.valueOf(obj);
                boolean isString = obj.getClass().equals(String.class);
                where = appendValue(where, columnNames[i], value, isString);
            }
        }
        if (limitOne) {
            where = limitOne(where);
        }
        return where;
    }

    protected static <T extends DbRecord> String buildDeleteQuery(Class<T> cls, String[] columnNames, Object[] values) {
        assert columnNames.length > 0;
        return "DELETE FROM " + DbHelper.getTableName(cls).value()
                + " " + buildWhereQuery(columnNames, values, false)
                + ";";
    }
}
