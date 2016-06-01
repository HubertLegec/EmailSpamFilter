package com.legec.tkom.core;

import com.legec.tkom.core.model.HeaderKey;
import com.legec.tkom.core.model.Token;
import com.legec.tkom.core.model.TokenType;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {

    static boolean isWhiteChar(char character) {
        return character == ' ' || character == '\t';
    }

    static boolean isSemicolonColonOrNewLine(char character) {
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
            QuotedPrintableCodec codec = new QuotedPrintableCodec(charset);
            try {
                return codec.decode(value, charset);
            } catch (DecoderException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new RuntimeException("Unknown coding");
        }
    }
}
