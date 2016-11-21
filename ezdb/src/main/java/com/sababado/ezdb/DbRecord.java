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
     * @throws SQLException in the case that there might have been a database error.
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

        TableName tableName = DbHelper.getTableName(cls);
        String tableNameStr = tableName.value();
        // TODO make that ID more generic, it doesn't support custom ID field names.
        String fields = DbHelper.getSelectColumns(cls, false, tableName, false, false).replace(tableNameStr + "." + Column.ID + ",", "");
        int numFields = fields.split(",").length;
        statement = "INSERT INTO " + tableNameStr +
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

        TableName tableName = DbHelper.getTableName(cls);
        String tableNameStr = tableName.value();
        String fields = DbHelper.getSelectColumns(cls, false, tableName, false, false)
                .replace(tableNameStr + "." + Column.ID + ",", "")
                .replace(",", "=?,")
                + "=?";
        statement = "UPDATE " + tableNameStr +
                " SET " + fields +
                " WHERE " + tableNameStr + "." + Column.ID + "=?;";

        updateStatements.put(cls, statement);
        return statement;
    }
}
