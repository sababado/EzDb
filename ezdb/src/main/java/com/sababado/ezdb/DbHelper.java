package com.sababado.ezdb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by robert on 9/15/15.
 */
public class DbHelper {
    private static final Logger _logger = Logger.getLogger(DbHelper.class.getName());
    public static HashMap<Class, String> classQueryMap = new HashMap<>(5);
    public static HashMap<Class, TableName> classTableNameMap = new HashMap<>(5);

    /**
     * An interface providing necessary parameters in order to connect to the database.
     */
    public interface ConnectionParams {
        /**
         * Specify the DB's URL to use for connection.
         *
         * @return The URL to use to connect to the database.
         */
        String getConnectionUrl();
    }

    public static Connection openConnection(ConnectionParams options) {
        String url = options.getConnectionUrl();

        if (url == null) {
            return null;
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            _logger.severe(e.getMessage());
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            _logger.warning("Couldn't close connection.\n" + e.getMessage());
        }
    }

    public static <T extends DbRecord> int getCount(Connection connection, Class<T> cls) {
        String tableName = getTableName(cls).value();
        String query = "SELECT COUNT(*) AS count FROM " + tableName + ";";
        int count = 0;
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            if (resultSet.first()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            _logger.warning("Trouble getting count for class " + cls.getSimpleName() + "\n" + e.getMessage());
        }
        return count;
    }

    public static <T extends DbRecord> List<T> getList(Connection connection, Class<T> cls) {
        return getList(connection, cls, null);
    }

    public static <T extends DbRecord> List<T> getList(Connection connection, Class<T> cls, String where) {
        return getList(connection, cls, false, where);
    }

    public static <T extends DbRecord> List<T> getList(Connection connection, Class<T> cls, boolean hasFk, String where) {
        String query = classQueryMap.get(cls);
        if (query == null) {
            query = buildQuery(cls, hasFk);
            classQueryMap.put(cls, query);
        }
        if (!StringUtils.isEmptyOrWhitespace(where)) {
            query = insertWhereClause(query, where);
        }

        try {
            List<T> list = new ArrayList<>();
            // TODO Prone to SQL injection with that where clause.
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            resultSet.beforeFirst();
            while (resultSet.next()) {
                list.add(ModelFactory.newInstance(cls, resultSet));
            }
            return list;
        } catch (SQLException e) {
            _logger.warning("Trouble getting list for class " + cls.getSimpleName() + "\n" + e.getMessage());
            return null;
        }
    }

    static String insertWhereClause(String query, String where) {
        if (where == null) {
            return query;
        }
        if (query.toLowerCase().contains("where") && where.toLowerCase().contains("where")) {
            where = where.replace("where", "and");
        }
        if (query.charAt(query.length() - 1) == ';') {
            return query.substring(0, query.length() - 1) + " " + where + ";";
        }
        return query + " " + where;
    }

    static String buildQuery(Class cls, boolean hasFk) {
        TableName tableName = getTableName(cls);
        String selectColumns = getSelectColumns(cls, hasFk, tableName.value(), true);
        String query = "select " + selectColumns +
                " from " + tableName.value();
        if (!StringUtils.isEmptyOrWhitespace(tableName.joinTable())) {
            query += getForeignKeyClause(tableName);
        }
        return query + ";";
    }

    static String getForeignKeyClause(TableName tableName) {
        String[] joinTables = tableName.joinTable().split(",");
        String clause = " join " + tableName.joinTable() + " where ";
        for (int i = 0; i < joinTables.length; i++) {
            String joinTable = joinTables[i].trim();
            if (i > 0) {
                clause += " and ";
            }
            clause += tableName.value() + "." + joinTable.toLowerCase() + "Id = " + joinTable + ".id";
        }
        return clause;
    }

    static String getSelectColumns(Class cls, boolean isFk, String tableName, boolean allowIgnores) {
        String columns = "";
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String columnValue = getColumnValue(field, false, tableName, allowIgnores);
            if (columnValue != null) {
                if (columnValue.equals(Column.FK_COL_NAME)) {
                    String foreignTableName = getTableName(field.getType()).value();
                    columns += tableName + "." + foreignTableName.toLowerCase() + "Id,";
                    if (isFk) {
                        columns += getSelectColumns(field.getType(), true, foreignTableName, true) + ",";
                    }
                } else {
                    columns += columnValue + ",";
                }
            }
        }
        if (columns.length() > 0) {
            columns = columns.substring(0, columns.length() - 1);
        }
        return columns;
    }

    /**
     * Returns the column name of a field, or {@link Column#FK_COL_NAME} if it is a foreign key link.
     *
     * @param field
     * @param fromFk
     * @param tableName
     * @param allowIgnores if false, any ignore flag will render this column value null.
     * @return
     */
    static String getColumnValue(Field field, boolean fromFk, String tableName, boolean allowIgnores) {
        String columnName = null;
        Annotation[] annotations = field.getDeclaredAnnotations();
        if (annotations != null) {
            for (int i = 0; i < annotations.length; i++) {
                Annotation annotation = annotations[i];
                if (annotation.annotationType().equals(Column.class)) {
                    Column column = (Column) annotation;
                    columnName = null;
                    if (allowIgnores || !column.ignoreInQueryGenerator()) {
                        columnName = column.value();
                        if (fromFk && columnName.equals(Column.ID)) {
                            columnName = null;
                        } else {
                            columnName = tableName + "." + columnName;
                        }
                    }
                    break;
                } else if (annotation.annotationType().equals(Fk.class)) {
                    columnName = Column.FK_COL_NAME;
                    break;
                }
            }
        }
        return columnName;
    }

    static TableName getTableName(Class cls) {
        TableName tableName = classTableNameMap.get(cls);
        if (tableName != null) {
            return tableName;
        }
        Annotation[] annotations = cls.getDeclaredAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].annotationType().equals(TableName.class)) {
                tableName = (TableName) annotations[i];
                break;
            }
        }
        if (tableName == null) {
            throw new RuntimeException("Class " + cls.getSimpleName() + " should have a TableName annotation.");
        }
        classTableNameMap.put(cls, tableName);
        return tableName;
    }

    public static String getFkColumnName(String columnName, String tableName) {
        String capitalizedColumnName = columnName.toLowerCase();
        capitalizedColumnName = Character.toString(capitalizedColumnName.charAt(0)).toUpperCase() + capitalizedColumnName.substring(1);
        return tableName.toLowerCase() + capitalizedColumnName;
    }

    public static <T extends DbRecord> List<Object> getDistinctList(Connection connection, Class<T> cls, String distinctColumn, String where) {
        String tableName = getTableName(cls).value();
        String query = "select " + distinctColumn + " from " + tableName;
        if (!StringUtils.isEmptyOrWhitespace(where)) {
            query = insertWhereClause(query, where);
        }
        query += " group by(" + distinctColumn + ");";

        try {
            List<Object> list = new ArrayList<>();
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            resultSet.beforeFirst();
            while (resultSet.next()) {
                list.add(resultSet.getObject(distinctColumn));
            }
            return list;
        } catch (SQLException e) {
            _logger.warning("Trouble getting distinct values for class " + cls.getSimpleName() + "\n" + e.getMessage());
            return null;
        }
    }
}
