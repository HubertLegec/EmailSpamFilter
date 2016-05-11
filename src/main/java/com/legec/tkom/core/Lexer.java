package com.legec.tkom.core;

import com.legec.tkom.core.model.Token;
import com.legec.tkom.core.model.TokenPosition;
import com.legec.tkom.core.model.TokenType;

import java.util.HashMap;
import java.util.Map;

class Lexer {

    private static final Map<String, TokenType> HEADER_FIELDS = new HashMap<String, TokenType>(){
        {
            put(TokenType.DELIVERED_TO.getName(), TokenType.DELIVERED_TO);
            put(TokenType.FROM.getName(), TokenType.FROM);
            put(TokenType.TO.getName(), TokenType.TO);
            put(TokenType.BCC.getName(), TokenType.BCC);
            put(TokenType.DATE.getName(),TokenType.DATE);
            put(TokenType.CC.getName(), TokenType.CC);
            put(TokenType.SUBJECT.getName(), TokenType.SUBJECT);
            put(TokenType.RECEIVED.getName(), TokenType.RECEIVED);
            put(TokenType.REPLY_TO.getName(), TokenType.REPLY_TO);
            put(TokenType.RETURN_PATH.getName(), TokenType.RETURN_PATH);
            put(TokenType.MIME_VERSION.getName(), TokenType.MIME_VERSION);
            put(TokenType.CONTENT_TRANSFER_ENCODING.getName(), TokenType.CONTENT_TRANSFER_ENCODING);
            put(TokenType.CONTENT_TYPE.getName(), TokenType.CONTENT_TYPE);
            put(TokenType.MESSAGE_ID.getName(), TokenType.MESSAGE_ID);
            put(TokenType.SENDER.getName(), TokenType.SENDER);
        }
    };

    private InputTextReader inputTextReader;

    Lexer(InputTextReader inputTextReader){
        this.inputTextReader = inputTextReader;
    }

    public Token getNextToken(){
        if(!inputTextReader.hasNext()){
            return null;
        } else {
            char processedChar = inputTextReader.seeNextCharacter();
            if (inputTextReader.getPosition().isLineBegin() && isWhiteChar(processedChar)){
                TokenPosition pos = getTokenPosition();
                removeWhiteSpace();
                return new Token(TokenType.INDENTATION, pos);
            } else if(processedChar == '\n'){
                TokenPosition pos = getTokenPosition();
                inputTextReader.getNextCharacter();
                return new Token(TokenType.NEW_LINE, pos);
            } else if(processedChar == ';'){
                TokenPosition pos = getTokenPosition();
                inputTextReader.getNextCharacter();
                return new Token(TokenType.SEMICOLON, pos);
            } else if(processedChar == ':') {
                TokenPosition pos = getTokenPosition();
                inputTextReader.getNextCharacter();
                return new Token(TokenType.COLON, pos);
            } else {
                return processContent();
            }
        }
    }

    private Token processContent(){
        removeWhiteSpace();
        TokenPosition pos = getTokenPosition();
        String element = buildElement();
        return convertElementToToken(element, pos);
    }

    private Token convertElementToToken(String element, TokenPosition position){
        TokenType type = HEADER_FIELDS.get(element);
        if(type != null){
            return new Token(type, position);
        } else {
            if(element.startsWith(TokenType.BOUNDARY.getName())){
                return new Token(TokenType.BOUNDARY, extractValueFromBoundary(element), position);
            } else {
                return new Token(TokenType.STRING_VALUE, element, position);
            }
        }
    }

    private String buildElement(){
        StringBuilder builder = new StringBuilder();
        builderLoop(builder);
        /*
        Colon should break building of element only in case it is header field element
        If it is string value colon should be included
         */
        if(inputTextReader.seeNextCharacter() == ':' && HEADER_FIELDS.get(builder.toString()) == null){
            while (inputTextReader.seeNextCharacter() == ':'){
                builder.append(inputTextReader.getNextCharacter());
                builderLoop(builder);
            }
        }
        return builder.toString();
    }

    private void builderLoop(StringBuilder builder){
        while(inputTextReader.hasNext() && !isSemicolonColonOrNewLine(inputTextReader.seeNextCharacter())){
            char c = inputTextReader.getNextCharacter();
            if(c == '"'){
                builder.append(c);
                buildInsideQuotationMarks(builder);
            } else {
                builder.append(c);
            }
        }
    }

    private void buildInsideQuotationMarks(StringBuilder builder){
        while (inputTextReader.hasNext() && inputTextReader.seeNextCharacter() != '"'){
            builder.append(inputTextReader.getNextCharacter());
        }
        if(inputTextReader.hasNext()){
            builder.append(inputTextReader.getNextCharacter());
        }
    }

    private TokenPosition getTokenPosition() {
        return new TokenPosition(
                inputTextReader.getPosition().getLine() + 1,
                inputTextReader.getPosition().getPositionInLine() + 1);
    }

    private String extractValueFromBoundary(String boundary){
        String result = boundary.substring(boundary.indexOf('=')+1);
        if(result.charAt(0) == '"' && result.charAt(result.length() - 1) == '"'){
            return result.substring(1, result.length() - 1);
        }
        return result;
    }

    private void removeWhiteSpace(){
        while(inputTextReader.hasNext() && isWhiteChar(inputTextReader.seeNextCharacter())){
            inputTextReader.getNextCharacter();
        }
    }

    private boolean isWhiteChar(char character){
        return character == ' ' || character == '\t';
    }

    private boolean isSemicolonColonOrNewLine(char character){
        return character == ';' || character == '\n' || character == ':';
    }
}
