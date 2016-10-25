package com.sababado.ezdb;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by robert on 8/25/16.
 */
public class QueryHelperTest {

    @Test
    public void testBuildWhereQuery() {
        //test failed arguments
        String[] columnNames = {Column.ID, Pub.ROOT_CODE, Pub.IS_ACTIVE};
        Object[] columnValues = {null, "4200", true};
        try {
            QueryHelper.buildWhereQuery(columnNames, columnValues, true);
            fail();
        } catch (AssertionError e) {
            // Sweet.
        }

        String expected = "where rootCode = '4200' and isActive = true limit 1";
        columnValues = new Object[]{null, "4200", true};
        boolean limitOne = true;
        String actual = QueryHelper.buildWhereQuery(columnNames, columnValues, limitOne);
        assertEquals(expected, actual);

        expected = "where id = 43 and rootCode = '4200' and isActive = true limit 1";
        columnValues = new Object[]{43, "4200", true};
        actual = QueryHelper.buildWhereQuery(columnNames, columnValues, limitOne);
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildDeleteQuery() {
        String deviceToken = "AAAJFJFJFJFJFJFJ";
        String expected = "DELETE FROM Device where " + Device.DEVICE_TOKEN + " = '" + deviceToken + "';";
        String actual = QueryHelper.buildDeleteQuery(
                Device.class,
                new String[]{Device.DEVICE_TOKEN},
                new Object[]{deviceToken});
        assertEquals(expected, actual);
    }
}
