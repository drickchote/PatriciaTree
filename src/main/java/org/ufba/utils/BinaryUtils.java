package org.ufba.utils;

public class BinaryUtils {

    /**
     * @param text
     * @return
     *
     * adapted from https://mkyong.com/java/java-convert-string-to-binary/
     */

    public static String toBinary(String text) {
        StringBuilder result = new StringBuilder();
        char[] chars = text.toCharArray();
        for (char aChar : chars) {
            result.append(
                String.format("%8s", Integer.toBinaryString(aChar))
                    .replaceAll(" ", "0")
            );
        }
        return result.toString();
    }

    public static String toBinaryWithSpaces(String text) {
        StringBuilder result = new StringBuilder();
        char[] chars = text.toCharArray();
        for (char aChar : chars) {
            result.append(
                String.format("%8s", Integer.toBinaryString(aChar))
                    .replaceAll(" ", "0")+" "
            );
        }
        return result.toString();
    }

    public static int bitDifference(String word1, String word2){
        String w1 = toBinary(word1);
        String w2 = toBinary(word2);

        if(w1.length() > w2.length()){
            w2 = fillWithZero(w2, w1.length());
        } else {
            w1 = fillWithZero(w1, w2.length());
        }

        for(int i=0; i< w1.length(); i++){
            if(w1.charAt(i) != w2.charAt(i)){
                return i+1;
            }
        }
        return -1; // there is no bit difference
    }

    public static char binaryAt(String text, int position){
        return fillWithZero(toBinary(text), position).charAt(position -1);
    }

    public static String fillWithZero(String text, int length){
        if(text.length() >= length){
            return text;
        }

        String result = text;


        int numberOfZero = length - text.length();

        for(int i=0; i<=numberOfZero+1; i++){
            result+= "0";
        }

        return result;
    }

    public static int convertBytesToInt(byte[] bytes){
        return (bytes[0]<<24)&0xff000000|
            (bytes[1]<<16)&0x00ff0000|
            (bytes[2]<< 8)&0x0000ff00|
            (bytes[3]<< 0)&0x000000ff;
    }

}
