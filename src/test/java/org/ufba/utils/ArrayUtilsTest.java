package org.ufba.utils;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.ufba.Constants.END_OF_STRING;

class ArrayUtilsTest {

    @Test
    void copyStringToCharacter() {
        String hello = "hello";
        char [] result = new char[hello.length()];

        ArrayUtils.copyStringToCharacter(hello, result);
        assertTrue(Arrays.equals(hello.toCharArray(), result));
    }

    @Test
    void getStringFrom() {
        String text = "Hello"+END_OF_STRING+" world";

        String result = ArrayUtils.getStringFrom(text.toCharArray());
        assertEquals("Hello", result);
    }

    @Test
    void testGetStringFrom() {
        String text = "Hello"+END_OF_STRING+" world";

        String result = ArrayUtils.getStringFrom(text.getBytes());
        assertEquals("Hello", result);
    }


}