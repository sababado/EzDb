package com.sababado.ezdb;

/**
 * Created by robert on 9/16/15.
 */
class StringUtils {

    public static boolean isEmptyOrWhitespace(String string) {
        return string == null || string.trim().equals("");
    }

    /**
     * Return a comma separated string with <code>n</code> question marks in it.
     *
     * @param n Number of question marks to return.
     * @return A comma separated string of <code>n</code> question marks.
     */
    public static String generateQuestionString(int n) {
        if (n < 1) {
            return "";
        }
        String s = "?";
        for (int i = 1; i < n; i++) {
            s = s + ",?";
        }
        return s;
    }

    public static boolean isNumber(String str) {
        if (isEmptyOrWhitespace(str)) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
