package com.legec.tkom.core.model;

import static com.legec.tkom.core.model.TokenType.*;

public class Token {
    private TokenType tokenType;
    private String value = null;
    private TokenPosition position;

    public Token(TokenType tokenType, TokenPosition position) {
        this.tokenType = tokenType;
        this.position = position;
    }

    public Token(TokenType tokenType, String value, TokenPosition position) {
        this.value = value;
        this.tokenType = tokenType;
        this.position = position;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public TokenPosition getPosition() {
        return position;
    }

    public String getValue() {
        return value;
    }

    public String getWrappedValue(){
        if(tokenType == BOUNDARY){
            return "boundary=\"" + value + "\"";
        } else if (tokenType == CHARSET){
            return "charset=\"" + value + "\"";
        } else if (tokenType == NAME){
            return "name=\"" + value + "\"";
        } else if (tokenType == FILE_NAME){
            return "filename=\"" + value + "\"";
        } else {
            return value;
        }
    }

    public boolean isHeaderRowKey(){
        return tokenType.ordinal() >= 0 && tokenType.ordinal() < 17;
    }

    @Override
    public String toString(){
        return tokenType.name() + (value != null ? (" [ " + value + " ]") : "");
    }
}
