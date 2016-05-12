package com.legec.tkom.core.model;

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

    @Override
    public String toString(){
        return tokenType.name() + (value != null ? (" [ " + value + " ]") : "");
    }
}
