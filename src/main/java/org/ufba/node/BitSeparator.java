package org.ufba.node;

import org.ufba.enums.NodeType;
import org.ufba.utils.ArrayUtils;
import org.ufba.utils.BinaryUtils;

import java.util.Arrays;

import static org.ufba.Constants.*;

public class BitSeparator extends Node{
    int bit;
    char[] pointerLeft = new char[POINTER_SIZE];
    char[] pointerRight = new char[POINTER_SIZE];

    public BitSeparator(int bit){
        super(NodeType.bitSeparator);
        this.bit = bit;
        this.pointerLeft[0] = END_OF_STRING;
        this.pointerRight[0] = END_OF_STRING;
    }

    public BitSeparator(int bit, String pointerLeft, String pointerRight){
        super(NodeType.bitSeparator);
        this.bit = bit;
        ArrayUtils.copyStringToCharacter(pointerLeft, this.pointerLeft);
        ArrayUtils.copyStringToCharacter(pointerRight, this.pointerRight);
    }

    public BitSeparator(int bit, String pointerLeft, String pointerRight, String pointerToDisk){
        super(NodeType.bitSeparator);
        this.bit = bit;
        ArrayUtils.copyStringToCharacter(pointerLeft, this.pointerLeft);
        ArrayUtils.copyStringToCharacter(pointerRight, this.pointerRight);
        this.setPointerToDisk(pointerToDisk);
    }

    public static BitSeparator from(byte[] bytes){
        byte [] separatorBytes = Arrays.copyOfRange(bytes,0, Integer.BYTES);
        int bitDifference = BinaryUtils.convertBytesToInt(separatorBytes);

        int pointerLeftOffset = separatorBytes.length;
        int pointerLeftOffsetEnd = pointerLeftOffset+POINTER_SIZE;
        byte [] pointerLeftBytes = Arrays.copyOfRange(bytes,pointerLeftOffset, pointerLeftOffsetEnd);
        String pointerLeft = ArrayUtils.getStringFrom(pointerLeftBytes);

        int pointerRightOffset = separatorBytes.length + pointerLeftBytes.length;
        int pointerRightOffsetEnd = pointerRightOffset+POINTER_SIZE;
        byte [] pointerRightBytes = Arrays.copyOfRange(bytes,pointerRightOffset, pointerRightOffsetEnd);
        String pointerRight = ArrayUtils.getStringFrom(pointerRightBytes);

        int pointerToDiskOffset = separatorBytes.length + pointerLeftBytes.length + pointerRightBytes.length;
        int pointerToDiskOffsetEnd = pointerToDiskOffset+POINTER_SIZE;
        byte [] pointerToDiskBytes = Arrays.copyOfRange(bytes,pointerToDiskOffset, pointerToDiskOffsetEnd);
        String pointerToDisk = ArrayUtils.getStringFrom(pointerToDiskBytes);

        return new BitSeparator(bitDifference, pointerLeft, pointerRight, pointerToDisk);
    }

    public static int getBytesLength(){
        return Integer.BYTES + POINTER_SIZE * 3;
    }

    public int getBit() {
        return bit;
    }

    public void setBit(int bit) {
        this.bit = bit;
    }

    public String getPointerLeft() {
        return ArrayUtils.getStringFrom(pointerLeft);
    }

    public char[] getPointerLeftCharacters() {
        return pointerLeft;
    }

    public void setPointerLeft(char[] pointerLeft) {
        this.pointerLeft = pointerLeft;
    }

    public void setAnotherPointer(char [] pointer){
        if(getPointerLeft().equals("")){
            this.pointerLeft = pointer;
            return;
        }

        if(getPointerRight().equals("")){
            this.pointerRight = pointer;
            return;
        }

        throw new Error("Both pointer is completed");
    }

    public String getPointerRight() {
        return ArrayUtils.getStringFrom(pointerRight);
    }

    public char[] getPointerRightCharacters() {
        return pointerRight;
    }

    public void setPointerRight(char[] pointerRight) {
        this.pointerRight = pointerRight;
    }
}
