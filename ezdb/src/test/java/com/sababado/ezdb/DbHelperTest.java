package com.sababado.ezdb;


import com.sababado.ezdb.models.Pub;
import com.sababado.ezdb.models.PubDevices;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * Created by robert on 9/15/15.
 */
public class DbHelperTest {
    @Test
    public void testGetColumnValue() throws NoSuchFieldException {
        Field field = Pub.class.getDeclaredField("id");
        String actual = DbHelper.getColumnValue(field, false, DbHelper.getTableName(Pub.class), true, true);
        String expected = "Pub.id AS pid";
        assertEquals(expected, actual);

        field = Pub.class.getDeclaredField("id");
        actual = DbHelper.getColumnValue(field, true, DbHelper.getTableName(Pub.class), true, true);
        expected = null;
        assertEquals(expected, actual);

        field = Pub.class.getDeclaredField(Pub.LAST_UPDATED);
        actual = DbHelper.getColumnValue(field, true, DbHelper.getTableName(Pub.class), false, true);
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
        String expected = "pub.id as pid,pub.fullcode as pfullcode,pub.rootcode as prootcode,pub.code as pcode,pub.version as pversion,pub.isactive as pisactive,pub.pubtype as ppubtype,pub.title as ptitle,pub.readabletitle as preadabletitle,pub.lastupdated as plastupdated".toLowerCase();
        String actual = DbHelper.getSelectColumns(Pub.class, false, DbHelper.getTableName(Pub.class), true, true).trim().toLowerCase();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSelectColumnsFk() {
        String expected = "PubDevices.id,PubDevices.deviceId,Device.id AS did,Device.deviceToken AS ddeviceToken,Device.lastNotificationFail AS dlastNotificationFail,Device.keepAlive AS dkeepAlive,PubDevices.pubId,Pub.id AS pid,Pub.fullCode AS pfullCode,Pub.rootCode AS prootCode,Pub.code AS pcode,Pub.version AS pversion,Pub.isActive AS pisActive,Pub.pubType AS ppubType,Pub.title AS ptitle,Pub.readableTitle AS preadableTitle,Pub.lastUpdated AS plastUpdated";
        String actual = DbHelper.getSelectColumns(PubDevices.class, true, DbHelper.getTableName(PubDevices.class), true, true).trim();
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

            @Override
            public String alias() {
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

            @Override
            public String alias() {
                return null;
            }
        };

        expected = " join First, SecondName where Name.firstId = First.id and Name.secondnameId = SecondName.id";
        actual = DbHelper.getForeignKeyClause(tableName);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTableName() {
        // Get easy table name
        String expected = "Pub";
        String actual = DbHelper.getTableName(Pub.class).value();
        assertEquals(expected, actual);

        // Get abstract table name
        actual = new Pub().tableName;
        assertEquals(expected, actual);
    }
}
