package com.legec.tkom.core;

import com.legec.tkom.core.exception.UnexpectedCharacterException;
import com.legec.tkom.core.model.Token;
import com.legec.tkom.core.model.TokenPosition;
import com.legec.tkom.core.model.TokenType;
import javafx.util.Pair;

import java.util.*;
import java.util.regex.Pattern;

import static com.legec.tkom.core.Utils.*;
import static com.legec.tkom.core.model.TokenType.*;
import static java.util.regex.Pattern.compile;

class Lexer {

    private static final Map<String, TokenType> CONST_HEADER_FIELDS_AND_VALUES = new HashMap<String, TokenType>(){
        {
            put(DELIVERED_TO.getPattern(), DELIVERED_TO);
            put(FROM.getPattern(), FROM);
            put(TO.getPattern(), TO);
            put(BCC.getPattern(), BCC);
            put(DATE.getPattern(),DATE);
            put(CC.getPattern(), CC);
            put(SUBJECT.getPattern(), SUBJECT);
            put(RECEIVED.getPattern(), RECEIVED);
            put(REPLY_TO.getPattern(), REPLY_TO);
            put(RETURN_PATH.getPattern(), RETURN_PATH);
            put(MIME_VERSION.getPattern(), MIME_VERSION);
            put(CONTENT_TRANSFER_ENCODING.getPattern(), CONTENT_TRANSFER_ENCODING);
            put(CONTENT_TYPE.getPattern(), CONTENT_TYPE);
            put(CONTENT_DISPOSITION.getPattern(), CONTENT_DISPOSITION);
            put(MESSAGE_ID.getPattern(), MESSAGE_ID);
            put(SENDER.getPattern(), SENDER);
            put(ENCODING_BASE64.getPattern(), ENCODING_BASE64);
            put(ENCODING_QUOTED_PRINTABLE.getPattern(), ENCODING_QUOTED_PRINTABLE);
        }
    };

    private static final List<Pair<Pattern, TokenType>> PATTERN_FIELDS_AND_VALUES = new ArrayList<Pair<Pattern, TokenType>>(){
        {
            add(new Pair<>(compile(BOUNDARY.getPattern()), BOUNDARY));
            add(new Pair<>(compile(SEPARATOR.getPattern()), SEPARATOR));
            add(new Pair<>(compile(NUMBER.getPattern()), NUMBER));
            add(new Pair<>(compile(CHARSET.getPattern()), CHARSET));
            add(new Pair<>(compile(CONTENT_MULTIPART.getPattern()), CONTENT_MULTIPART));
            add(new Pair<>(compile(CONTENT_TEXT.getPattern()), CONTENT_TEXT));
            add(new Pair<>(compile(CONTENT_IMAGE.getPattern()), CONTENT_IMAGE));
            add(new Pair<>(compile(CONTENT_APPLICATION.getPattern()), CONTENT_APPLICATION));
            add(new Pair<>(compile(FILE_NAME.getPattern()), FILE_NAME));
        }
    };

    private InputTextReader inputTextReader;
    private TokenPosition currentTokenPosition;

    Lexer(InputTextReader inputTextReader){
        this.inputTextReader = inputTextReader;
    }

    public Token getNextToken() throws UnexpectedCharacterException {
        if(!inputTextReader.hasNext()){
            return null;
        } else {
            char processedChar = inputTextReader.seeNextCharacter();
            if (inputTextReader.getPosition().isLineBegin() && isWhiteChar(processedChar)){
                currentTokenPosition = getTokenPosition();
                removeWhiteSpace();
                return new Token(INDENTATION, currentTokenPosition);
            } else if(processedChar == '\n') {
                currentTokenPosition = getTokenPosition();
                inputTextReader.getNextCharacter();
                return new Token(NEW_LINE, currentTokenPosition);
            } else if(processedChar == ';') {
                currentTokenPosition = getTokenPosition();
                inputTextReader.getNextCharacter();
                return new Token(SEMICOLON, currentTokenPosition);
            } else if(processedChar == ':') {
                currentTokenPosition = getTokenPosition();
                inputTextReader.getNextCharacter();
                return new Token(COLON, currentTokenPosition);
            } else if(processedChar == ' ') {
                currentTokenPosition = getTokenPosition();
                inputTextReader.getNextCharacter();
                return new Token(SPACE, currentTokenPosition);
            } else {
                return processContent();
            }
        }
    }

    private Token processContent() throws UnexpectedCharacterException {
        currentTokenPosition = getTokenPosition();
        String element = buildElement();
        return convertElementToToken(element, currentTokenPosition);
    }

    private Token convertElementToToken(String element, TokenPosition position){
        TokenType type = CONST_HEADER_FIELDS_AND_VALUES.get(element);
        if(type != null){
            return new Token(type, position);
        } else {
            Token tokenToReturn = null;
            Optional<TokenType> optType = PATTERN_FIELDS_AND_VALUES.stream()
                    .filter(it -> matchPattern(element, it.getKey()))
                    .map(Pair::getValue)
                    .findAny();
            if(optType.isPresent()){
                switch (optType.get()){
                    case CONTENT_IMAGE:
                    case CONTENT_MULTIPART:
                    case CONTENT_TEXT:
                    case CONTENT_APPLICATION:
                        tokenToReturn = new Token(optType.get(), position);
                        break;
                    case BOUNDARY:
                    case CHARSET:
                    case FILE_NAME:
                        tokenToReturn = new Token(optType.get(), getStringBetweenQuotationMarks(element), position);
                        break;
                    case NUMBER:
                        tokenToReturn = new Token(optType.get(), element, position);
                        break;
                    case SEPARATOR:
                        tokenToReturn = new Token(SEPARATOR, element.substring(2, element.length() - 2), position);
                        break;
                }
            } else {
                tokenToReturn = new Token(STRING, element, position);
            }
            return tokenToReturn;
        }
    }

    private String buildElement() throws UnexpectedCharacterException{
        StringBuilder builder = new StringBuilder();
        builderLoop(builder);
        /*
        Colon should break building of element only in case it is header field element
        If it is string value colon should be included
         */
        if(inputTextReader.seeNextCharacter() == ':' && CONST_HEADER_FIELDS_AND_VALUES.get(builder.toString()) == null){
            while (inputTextReader.seeNextCharacter() == ':'){
                builder.append(inputTextReader.getNextCharacter());
                builderLoop(builder);
            }
        }
        return builder.toString();
    }

    private void builderLoop(StringBuilder builder) throws UnexpectedCharacterException {
        while(inputTextReader.hasNext() && isValidStringCharacter(inputTextReader.seeNextCharacter())){
            char c = inputTextReader.getNextCharacter();
            if(c == '"'){
                builder.append(c);
                buildInsideQuotationMarks(builder);
            } else {
                builder.append(c);
            }
        }
    }

    private void buildInsideQuotationMarks(StringBuilder builder) throws UnexpectedCharacterException{
        while (inputTextReader.hasNext() && inputTextReader.seeNextCharacter() != '"'){
            builder.append(inputTextReader.getNextCharacter());
        }
        if(inputTextReader.hasNext()){
            builder.append(inputTextReader.getNextCharacter());
        } else {
            throw new UnexpectedCharacterException("EOF", "\"", currentTokenPosition);
        }
    }

    private TokenPosition getTokenPosition() {
        return new TokenPosition(
                inputTextReader.getPosition().getLine() + 1,
                inputTextReader.getPosition().getPositionInLine() + 1);
    }

    private void removeWhiteSpace(){
        while(inputTextReader.hasNext() && isWhiteChar(inputTextReader.seeNextCharacter())){
            inputTextReader.getNextCharacter();
        }
    }
}
