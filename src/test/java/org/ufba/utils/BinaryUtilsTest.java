package org.ufba.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BinaryUtilsTest {

    @Test
    void toBinary() {
        String helloWorld = "Hello world";
        String helloWorldInBinary = "0100100001100101011011000110110001101111001000000111011101101111011100100110110001100100";
        String result = BinaryUtils.toBinary(helloWorld);

        assertEquals(helloWorldInBinary, result);
    }

    @Test
    void bitDifference(){
        String word1 = "belo"; // 01100010 01100101 01101100 01101111 ;
        String word2 = "bela"; // 01100010 01100101 01101100 01100001 ;
        String word3 = "bel";  // 01100010 01100101 01101100 00000000

        assertEquals(28, BinaryUtils.bitDifference(word1, word2));
        assertEquals(25, BinaryUtils.bitDifference(word2, word3));
        assertEquals(-1, BinaryUtils.bitDifference(word1, word1));
        assertEquals(-1, BinaryUtils.bitDifference(word2, word2));
        assertEquals(-1, BinaryUtils.bitDifference(word3, word3));
    }

    @Test
    void fillWithZero(){
        String word1 = "belo"; // 01100010 01100101 01101100 01101111 ;
        String word3 = "bel";  // 01100010 01100101 01101100 00000000

        String filled = BinaryUtils.fillWithZero(BinaryUtils.toBinary(word3), BinaryUtils.toBinary(word1).length());
        assertEquals("01100010011001010110110000000000", filled);
    }

}