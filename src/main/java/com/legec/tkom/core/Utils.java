package com.legec.tkom.core;

import java.util.regex.Pattern;

public class Utils {

    public static boolean isWhiteChar(char character){
        return character == ' ' || character == '\t';
    }

    public static boolean isSemicolonColonOrNewLine(char character) {
        return character == ';' || character == '\n' || character == ':';
    }

    public static boolean isValidStringCharacter(char character) {
        return !isWhiteChar(character) && !isSemicolonColonOrNewLine(character);
    }

    public static String getStringBetweenQuotationMarks(String string){
        try {
            return string.substring(string.indexOf('"') + 1, string.lastIndexOf('"'));
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public static boolean matchPattern(String element, Pattern pattern){
        return pattern.matcher(element).matches();
    }
}
