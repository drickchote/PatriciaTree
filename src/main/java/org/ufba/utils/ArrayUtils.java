package org.ufba.utils;

import static org.ufba.Constants.END_OF_STRING;

public class ArrayUtils {

    public static void copyStringToCharacter(String source, char [] destination){
        int i;

        for(i=0; i< source.length(); i++){
            destination[i] = source.charAt(i);
        }

//      ADD END_OF_STRING character flag if destination array is greater than source string
        if(source.length() < destination.length){
            destination[i] = END_OF_STRING;
        }
    }

    public static String getStringFrom(char [] source){
        String string = "";

        for(int i=0; i< source.length; i++){
            if(source[i] == END_OF_STRING) break;
            string += source[i];
        }

        return string;
    }

    public static String getStringFrom(byte [] source){
        return getStringFrom(new String(source).toCharArray());
    }

}
