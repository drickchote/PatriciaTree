package org.ufba.node;

import org.ufba.utils.FileUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitSeparatorTest {

    @org.junit.jupiter.api.Test
    void from() {

        BitSeparator bitSeparator = new BitSeparator(5, "pointer_Left", "pointer_Right", "pointerToDisk_0");

        byte[] bytes = FileUtils.convertNodeToBytes(bitSeparator);

        BitSeparator createdBitSeparator = BitSeparator.from(bytes);

        assertEquals(bitSeparator.getBit(), createdBitSeparator.getBit());
        assertEquals(bitSeparator.getPointerLeft(), createdBitSeparator.getPointerLeft());
        assertEquals(bitSeparator.getPointerRight(), createdBitSeparator.getPointerRight());
        assertEquals(bitSeparator.getPointerToDisk(), createdBitSeparator.getPointerToDisk());

    }
}