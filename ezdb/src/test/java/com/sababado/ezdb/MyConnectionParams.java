package com.sababado.ezdb;

/**
 * Created by robert on 10/25/16.
 */

class MyConnectionParams implements DbHelper.ConnectionParams {
    @Override
    public String getConnectionUrl() {
        try {
//            if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
//                // Load the class that provides the new "jdbc:google:mysql://" prefix.
//                Class.forName("com.mysql.jdbc.GoogleDriver");
//                return StringUtils.PROD_DB;
//            } else {
            // Local MySQL instance to use during development.
            Class.forName("com.mysql.jdbc.Driver");
            return "jdbc:mysql://127.0.0.1:8889/mcPubsDev?user=root&zeroDateTimeBehavior=convertToNull";
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
