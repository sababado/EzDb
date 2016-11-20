package com.sababado.ezdb.models;

import com.sababado.ezdb.DbHelper;
import com.sababado.ezdb.DbRecord;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by robert on 11/20/16.
 */
public abstract class AbstractDbRecord extends DbRecord {
    public String tableName;

    public AbstractDbRecord() {
        tableName = DbHelper.getTableName(this.getClass()).value();
    }

    public AbstractDbRecord(ResultSet resultSet) throws SQLException {
        super(resultSet);
    }
}
