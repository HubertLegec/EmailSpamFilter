package com.legec.tkom.core;

import com.legec.tkom.core.model.HeaderKey;
import com.legec.tkom.core.model.Token;
import com.legec.tkom.core.model.TokenType;

import java.util.List;
import java.util.regex.Pattern;

class Utils {

    static boolean isWhiteChar(char character) {
        return character == ' ' || character == '\t';
    }

    static boolean isSemicolonColonOrNewLine(char character) {
        return character == ';' || character == '\n' || character == ':';
    }

    static boolean isValidStringCharacter(char character) {
        return !isWhiteChar(character) && !isSemicolonColonOrNewLine(character);
    }

    static String getStringBetweenQuotationMarks(String string) {
        try {
            return string.substring(string.indexOf('"') + 1, string.lastIndexOf('"'));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    static boolean matchPattern(String element, Pattern pattern) {
        return pattern.matcher(element).matches();
    }

    static boolean isTokenTypeIn(Token token, List<TokenType> types) {
        return types.contains(token.getTokenType());
    }

    static boolean isHeaderKeyIn(HeaderKey key, List<HeaderKey> values) {
        return values.contains(key);
    }
}
