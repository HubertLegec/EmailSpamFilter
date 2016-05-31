package com.legec.tkom.core.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.legec.tkom.core.model.TokenType.*;
import static com.legec.tkom.core.model.TokenType.ENCODING_VAL;
import static java.util.regex.Pattern.compile;

public class Mappings {
    //--------------------------------------- Lexer ---------------------------------------------------------
    public static final Map<String, TokenType> CONST_HEADER_FIELDS_AND_VALUES = new HashMap<String, TokenType>(){
        {
            put(DELIVERED_TO.getPattern(), DELIVERED_TO);
            put(FROM.getPattern(), FROM);
            put(TO.getPattern(), TO);
            put(BCC.getPattern(), BCC);
            put(DATE.getPattern(),DATE);
            put(CC.getPattern(), CC);
            put(SUBJECT.getPattern(), SUBJECT);
            put(RECEIVED.getPattern(), RECEIVED);
            put(X_RECEIVED.getPattern(), X_RECEIVED);
            put(REPLY_TO.getPattern(), REPLY_TO);
            put(RETURN_PATH.getPattern(), RETURN_PATH);
            put(MIME_VERSION.getPattern(), MIME_VERSION);
            put(CONTENT_TRANSFER_ENCODING.getPattern(), CONTENT_TRANSFER_ENCODING);
            put(CONTENT_TYPE.getPattern(), CONTENT_TYPE);
            put(CONTENT_DISPOSITION.getPattern(), CONTENT_DISPOSITION);
            put(MESSAGE_ID.getPattern(), MESSAGE_ID);
            put(SENDER.getPattern(), SENDER);
            put(LIST_UNSUBSCRIBE.getPattern(), LIST_UNSUBSCRIBE);
        }
    };

    public static final List<Pair<Pattern, TokenType>> PATTERN_FIELDS_AND_VALUES = new ArrayList<Pair<Pattern, TokenType>>(){
        {
            add(new Pair<>(compile(BOUNDARY.getPattern()), BOUNDARY));
            add(new Pair<>(compile(SEPARATOR.getPattern()), SEPARATOR));
            add(new Pair<>(compile(CHARSET.getPattern()), CHARSET));
            add(new Pair<>(compile(CONTENT_TYPE_VAL.getPattern()), CONTENT_TYPE_VAL));
            add(new Pair<>(compile(FILE_NAME.getPattern()), FILE_NAME));
            add(new Pair<>(compile(NAME.getPattern()), NAME));
            add(new Pair<>(compile(ENCODING_VAL.getPattern()), ENCODING_VAL));
            add(new Pair<>(compile(DISPOSITION_VAL.getPattern()), DISPOSITION_VAL));
        }
    };

    //----------------------------------------- Parser -----------------------------------------------------------------
    private static final List<TokenType> CONTENT_TYPE_VALUES = new ArrayList<TokenType>(){
        {
            add(BOUNDARY);
            add(CHARSET);
            add(CONTENT_TYPE_VAL);
            add(NAME);
        }
    };

    private static final List<TokenType> CONTENT_DISPOSITION_VALUES = new ArrayList<TokenType>(){
        {
            add(DISPOSITION_VAL);
            add(FILE_NAME);
        }
    };

    private static final List<TokenType> CONTENT_TRANSFER_ENCODING_VALUES = new ArrayList<TokenType>(){
        {
            add(ENCODING_VAL);
        }
    };

    public static final Map<HeaderKey, List<TokenType>> HEADER_KEY_VALUES = new HashMap<HeaderKey, List<TokenType>>(){
        {
            put(HeaderKey.CONTENT_TYPE, CONTENT_TYPE_VALUES);
            put(HeaderKey.CONTENT_DISPOSITION, CONTENT_DISPOSITION_VALUES);
            put(HeaderKey.CONTENT_TRANSFER_ENCODING, CONTENT_TRANSFER_ENCODING_VALUES);
        }
    };
}
