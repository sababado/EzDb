package com.sababado.ezdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robert on 9/15/15.
 */
public abstract class DbRecord {
    public static final String ID = Column.ID;
    static Map<Class<? extends DbRecord>, String> insertStatements = new HashMap<>(3);
    static Map<Class<? extends DbRecord>, String> updateStatements = new HashMap<>(3);

    public DbRecord() {
    }

    /**
     * Initializes the record with the result set's current position.
     *
     * @param resultSet Result set on the position to initialize from.
     */
    public DbRecord(ResultSet resultSet) throws SQLException {

    }

    public abstract Long getId();

    public abstract void setId(Long id);

    protected static String getInsertQuery(Class<? extends DbRecord> cls) {
        String statement = insertStatements.get(cls);
        if (statement != null) {
            return statement;
        }

        String tableName = DbHelper.getTableName(cls).value();
        // TODO make that ID more generic, it doesn't support custom ID field names.
        String fields = DbHelper.getSelectColumns(cls, false, tableName, false).replace(tableName + "." + Column.ID + ",", "");
        int numFields = fields.split(",").length;
        statement = "INSERT INTO " + tableName +
                " (" + fields + ") " +
                "VALUES (" + StringUtils.generateQuestionString(numFields) + ");";

        insertStatements.put(cls, statement);
        return statement;
    }

    protected static String getUpdateQuery(Class<? extends DbRecord> cls) {
        String statement = updateStatements.get(cls);
        if (statement != null) {
            return statement;
        }

        String tableName = DbHelper.getTableName(cls).value();
        String fields = DbHelper.getSelectColumns(cls, false, tableName, false)
                .replace(tableName + "." + Column.ID + ",", "")
                .replace(",", "=?,")
                + "=?";
        statement = "UPDATE " + tableName +
                " SET " + fields +
                " WHERE " + tableName + "." + Column.ID + "=?;";

        updateStatements.put(cls, statement);
        return statement;
    }
}
