package com.sababado.ezdb;


import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * Created by robert on 9/15/15.
 */
public class DbHelperTest {
    @Test
    public void testGetTableName() {
        String expected = "Pub";
        String actual = DbHelper.getTableName(Pub.class).value();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetColumnValue() throws NoSuchFieldException {
        Field field = Pub.class.getDeclaredField("id");
        String actual = DbHelper.getColumnValue(field, false, DbHelper.getTableName(Pub.class).value(), true);
        String expected = "Pub.id";
        assertEquals(expected, actual);

        field = Pub.class.getDeclaredField("id");
        actual = DbHelper.getColumnValue(field, true, DbHelper.getTableName(Pub.class).value(), true);
        expected = null;
        assertEquals(expected, actual);

        field = Pub.class.getDeclaredField(Pub.LAST_UPDATED);
        actual = DbHelper.getColumnValue(field, true, DbHelper.getTableName(Pub.class).value(), false);
        expected = null;
        assertEquals(expected, actual);
//
//        field = CuttingScoreRecord.class.getDeclaredField("primaryMos");
//        actual = DbUtils.getColumnValue(field, false, DbUtils.getTableName(CuttingScoreRecord.class).value());
//        expected = Column.FK_COL_NAME;
//        assertEquals(expected, actual);

    }

    @Test
    public void testGetSelectColumns() {
        String expected = "Pub.id,Pub.fullCode,Pub.rootCode,Pub.code,Pub.version,Pub.isActive,Pub.pubType,Pub.title,Pub.readableTitle,Pub.lastUpdated".toLowerCase();
        String actual = DbHelper.getSelectColumns(Pub.class, false, DbHelper.getTableName(Pub.class).value(), true).trim().toLowerCase();
        assertEquals(expected, actual);
    }

//    @Test
//    public void testBuildQuery() {
//        String expected = ("select CuttingScore.id,CuttingScore.statusRank,CuttingScore.monthYear,CuttingScore.mosId,mos.code,mos.title,CuttingScore.score " +
//                "from CuttingScore " +
//                "join MOS where CuttingScore.mosId = mos.id;").toLowerCase();
//        String actual = DbUtils.buildQuery(CuttingScoreRecord.class).toLowerCase();
//        assertEquals(expected, actual);
//    }

    @Test
    public void testInsertWhereClause() {
        String query = "string;";
        String where = "hello";
        String expected = "string hello;";
        String actual = DbHelper.insertWhereClause(query, where);
        assertEquals(expected, actual);

        query = "string";
        expected = "string hello";
        actual = DbHelper.insertWhereClause(query, where);
        assertEquals(expected, actual);

        where = null;
        expected = query;
        actual = DbHelper.insertWhereClause(query, where);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetForeignKeyClause() {
        TableName tableName = new TableName() {
            @Override
            public String joinTable() {
                return "Single";
            }

            @Override
            public String value() {
                return "Name";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };

        String expected = " join Single where Name.singleId = Single.id";
        String actual = DbHelper.getForeignKeyClause(tableName);
        assertEquals(expected, actual);

        tableName = new TableName() {
            @Override
            public String joinTable() {
                return "First, SecondName";
            }

            @Override
            public String value() {
                return "Name";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };

        expected = " join First, SecondName where Name.firstId = First.id and Name.secondnameId = SecondName.id";
        actual = DbHelper.getForeignKeyClause(tableName);
        assertEquals(expected, actual);
    }
}
