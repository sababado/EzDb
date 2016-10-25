package com.sababado.ezdb;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by robert on 8/25/16.
 */
public class StringUtilsTest {
    @Test
    public void testIsEmptyOrWhitespace() {
        assertEquals(true, StringUtils.isEmptyOrWhitespace(""));
        assertEquals(true, StringUtils.isEmptyOrWhitespace("    "));
        assertEquals(true, StringUtils.isEmptyOrWhitespace(null));
        assertEquals(false, StringUtils.isEmptyOrWhitespace("nope"));
    }

    @Test
    public void testGenerateQuestionString() {
        assertEquals("", StringUtils.generateQuestionString(-124));
        assertEquals("", StringUtils.generateQuestionString(0));
        assertEquals("?", StringUtils.generateQuestionString(1));
        assertEquals("?,?", StringUtils.generateQuestionString(2));
        assertEquals("?,?,?,?,?", StringUtils.generateQuestionString(5));
    }
}
