package com.legec.tkom.core;

import com.legec.tkom.core.model.HeaderKey;
import com.legec.tkom.core.model.Token;
import com.legec.tkom.core.model.TokenType;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    static Pattern URL_PATTERN = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    static Pattern HTML_IMG_URL = Pattern.compile("\\B<a href=\"(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].*\"><img.*></a>");

    static boolean isWhiteChar(char character) {
        return character == ' ' || character == '\t';
    }

    private static boolean isSemicolonColonOrNewLine(char character) {
        return character == ';' || character == '\n' || character == ':';
    }

    static boolean isValidStringCharacter(char character) {
        return !isWhiteChar(character) && !isSemicolonColonOrNewLine(character);
    }

    public static String getStringBetweenQuotationMarks(String string) {
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
        return token != null && types.contains(token.getTokenType());
    }

    static boolean isHeaderKeyIn(HeaderKey key, List<HeaderKey> values) {
        return values.contains(key);
    }

    static String decodeString(String value, String coding, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        if ("base64".equals(coding)) {
            return new String(Base64.getDecoder().decode(value.getBytes(charset)));
        } else if ("quoted-printable".equals(coding)) {
            QuotedPrintableCodec codec = new QuotedPrintableCodec("iso-8859-2");
            try {
                return codec.decode(value, charset);
            } catch (DecoderException e) {
                return value;
            }
        } else {
            throw new RuntimeException("Unknown coding");
        }
    }

    static List<String> getAllPatternOccurencesFromText(String text, Pattern pattern){
        Matcher m = pattern.matcher(text);
        List<String> result = new ArrayList<>();
        while (m.find()){
            result.add(m.group());
        }
        return result;
    }

    static long stringIntersection(String s1, String s2){
        HashSet<Character> h1 = new HashSet<>();
        HashSet<Character> h2 = new HashSet<>();
        for(int i = 0; i < s1.length(); i++) {
            h1.add(s1.charAt(i));
        }
        for(int i = 0; i < s2.length(); i++) {
            h2.add(s2.charAt(i));
        }
        h1.retainAll(h2);
        return h1.stream().count();
    }

}
